package com.pollalgorand.rest.adapter.repository;

import static com.pollalgorand.rest.adapter.AlgorandUtils.headers;
import static com.pollalgorand.rest.adapter.AlgorandUtils.txHeaders;
import static com.pollalgorand.rest.adapter.AlgorandUtils.txValues;
import static com.pollalgorand.rest.adapter.AlgorandUtils.values;

import com.algorand.algosdk.account.Account;
import com.algorand.algosdk.crypto.TEALProgram;
import com.algorand.algosdk.transaction.SignedTransaction;
import com.algorand.algosdk.transaction.Transaction;
import com.algorand.algosdk.util.Encoder;
import com.algorand.algosdk.v2.client.common.AlgodClient;
import com.algorand.algosdk.v2.client.common.Response;
import com.algorand.algosdk.v2.client.model.PendingTransactionResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.pollalgorand.rest.adapter.PollTealParams;
import com.pollalgorand.rest.adapter.TealProgramFactory;
import com.pollalgorand.rest.adapter.converter.PollBlockchainParamsAdapter;
import com.pollalgorand.rest.adapter.exceptions.EncodeTransactionException;
import com.pollalgorand.rest.adapter.exceptions.InvalidMnemonicKeyException;
import com.pollalgorand.rest.adapter.exceptions.NodeStatusException;
import com.pollalgorand.rest.adapter.exceptions.RetrievingApplicationIdException;
import com.pollalgorand.rest.adapter.exceptions.SendingTransactionException;
import com.pollalgorand.rest.adapter.exceptions.SignTransactionException;
import com.pollalgorand.rest.adapter.exceptions.WaitingTransactionConfirmationException;
import com.pollalgorand.rest.adapter.service.BuildTransactionService;
import com.pollalgorand.rest.domain.model.BlockchainPoll;
import com.pollalgorand.rest.domain.model.Poll;
import com.pollalgorand.rest.domain.repository.BlockchainPollRepository;
import java.security.GeneralSecurityException;
import java.security.NoSuchAlgorithmException;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AlgorandASCPollRepository implements BlockchainPollRepository {

  private Logger logger = LoggerFactory.getLogger(AlgorandASCPollRepository.class);

  private AlgodClient algodClient;
  private TealProgramFactory tealProgramFactory;
  private PollBlockchainParamsAdapter pollBlockchainParamsAdapter;
  private BuildTransactionService buildTransactionService;

  public AlgorandASCPollRepository(AlgodClient algodClient,
      TealProgramFactory tealProgramFactory,
      PollBlockchainParamsAdapter pollBlockchainParamsAdapter,
      BuildTransactionService buildTransactionService) {

    this.algodClient = algodClient;
    this.tealProgramFactory = tealProgramFactory;
    this.pollBlockchainParamsAdapter = pollBlockchainParamsAdapter;
    this.buildTransactionService = buildTransactionService;
  }

  @Override
  public Optional<BlockchainPoll> save(Poll poll) {

    Transaction unsignedTx = createUnsignedTxFor(poll);

    Account account;
    SignedTransaction signedTx;

    try {

      //signer service? con collaboratore che crea account
      account = new Account(poll.getMnemonicKey());
      signedTx = account.signTransaction(unsignedTx);
      byte[] encodedTxBytes = Encoder.encodeToMsgPack(signedTx);

      String transactionId = algodClient.RawTransaction().rawtxn(encodedTxBytes).execute(txHeaders, txValues).body().txId;

      //collaboratore
      waitForConfirmation(transactionId);

      Long appId = getApplicationId(transactionId);

      //introduce adapter
      return Optional.of(new BlockchainPoll(appId, poll.getName(), poll.getSender(), poll.getStartSubscriptionTime(), poll.getEndSubscriptionTime(),
          poll.getStartVotingTime(), poll.getEndVotingTime(), poll.getOptions(), poll.getMnemonicKey(), poll.getDescription()));

    } catch (NoSuchAlgorithmException e) {
      logger.error("Something goes wrong signing transaction", e);
      throw new SignTransactionException(e, poll.getName());
    } catch (GeneralSecurityException e) {
      logger.error("Something goes wrong with mnemonic key transaction", e);
      throw new InvalidMnemonicKeyException(e, poll.getName());
    } catch (JsonProcessingException e) {
      logger.error("Something goes wrong encoding transaction", e);
      throw new EncodeTransactionException(e, poll.getName());
    } catch (Exception e) {
      logger.error("Something goes wrong sending transaction", e);
      throw new SendingTransactionException(e, poll.getName());
    }

  }

  private Long getApplicationId(String transactionId) {
    PendingTransactionResponse pTrx;
    try {
      pTrx = algodClient.PendingTransactionInformation(transactionId).execute(headers, values).body();
    } catch (Exception e) {
      throw new RetrievingApplicationIdException(e, transactionId);
    }
    return pTrx.applicationIndex;
  }

  @Override
  public Transaction createUnsignedTxFor(Poll poll) {

    Long lastRound = getBlockChainLastRound();

    PollTealParams pollTealParams = pollBlockchainParamsAdapter
        .fromPollToPollTealParams(poll, lastRound);

    TEALProgram approvalProgram = tealProgramFactory.createApprovalProgramFrom(pollTealParams);
    TEALProgram clearStateProgram = tealProgramFactory.createClearStateProgram();

    return buildTransactionService.buildTransaction(pollTealParams, approvalProgram, clearStateProgram, poll.getSender());
  }

  private Long getBlockChainLastRound() {

    Long lastRound;
    try {
      lastRound = algodClient.GetStatus().execute(headers, values).body().lastRound;
    } catch (Exception e) {
      logger.error("Something goes wrong getting last blockchain round", e);
      throw new NodeStatusException(e);
    }
    return lastRound;
  }

  private void waitForConfirmation(String txID) {

    try {
      Long lastRound = algodClient.GetStatus().execute(headers,values).body().lastRound;
      while (true) {

        // Check the pending transactions
        Response<PendingTransactionResponse> pendingInfo = algodClient
            .PendingTransactionInformation(txID).execute(headers,values);
        if (pendingInfo.body().confirmedRound != null && pendingInfo.body().confirmedRound > 0) {
          // Got the completed Transaction
          System.out.println(
              "Transaction " + txID + " confirmed in round " + pendingInfo.body().confirmedRound);
          break;
        }
        lastRound++;
        algodClient.WaitForBlock(lastRound).execute(headers,values);
      }
    } catch (Exception e) {
      throw new WaitingTransactionConfirmationException(e, txID);
    }
  }
}
