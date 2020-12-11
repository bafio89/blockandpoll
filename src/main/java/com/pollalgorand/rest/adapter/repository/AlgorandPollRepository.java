package com.pollalgorand.rest.adapter.repository;

import static java.util.Arrays.asList;

import com.algorand.algosdk.account.Account;
import com.algorand.algosdk.crypto.TEALProgram;
import com.algorand.algosdk.logic.StateSchema;
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
import com.pollalgorand.rest.adapter.exceptions.BlockChainParameterException;
import com.pollalgorand.rest.adapter.exceptions.EncodeTransactionException;
import com.pollalgorand.rest.adapter.exceptions.InvalidMnemonicKeyException;
import com.pollalgorand.rest.adapter.exceptions.InvalidSenderAddressException;
import com.pollalgorand.rest.adapter.exceptions.NodeStatusException;
import com.pollalgorand.rest.adapter.exceptions.RetrievingApplicationIdException;
import com.pollalgorand.rest.adapter.exceptions.SendingTransactionException;
import com.pollalgorand.rest.adapter.exceptions.SignTransactionException;
import com.pollalgorand.rest.adapter.exceptions.WaitingTransactionConfirmationException;
import com.pollalgorand.rest.domain.model.BlockchainPoll;
import com.pollalgorand.rest.domain.model.Poll;
import com.pollalgorand.rest.domain.repository.PollRepository;
import java.security.GeneralSecurityException;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Optional;
import org.apache.commons.lang3.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AlgorandPollRepository implements PollRepository {

  private Logger logger = LoggerFactory.getLogger(AlgorandPollRepository.class);

  private AlgodClient algodClient;
  private TealProgramFactory tealProgramFactory;
  private PollBlockchainParamsAdapter pollBlockchainParamsAdapter;

  private final String[] headers = {"X-API-Key"};
  private final String[] values = {"KmeYVcOTUFayYL9uVy9mI9d7dDewlWth7pprTlo9"};
  private final String[] txHeaders = ArrayUtils.add(headers, "Content-Type");
  private final String[] txValues = ArrayUtils.add(values, "application/x-binary");

  public AlgorandPollRepository(AlgodClient algodClient,
      TealProgramFactory tealProgramFactory,
      PollBlockchainParamsAdapter pollBlockchainParamsAdapter) {

    this.algodClient = algodClient;
    this.tealProgramFactory = tealProgramFactory;
    this.pollBlockchainParamsAdapter = pollBlockchainParamsAdapter;
  }

  @Override
  public Optional<Poll> save(Poll poll) {

    Transaction unsignedTx = createUnsignedTxFor(poll);

    Account account;
    SignedTransaction signedTx;
    try {
      account = new Account(poll.getMnemonicKey());

      signedTx = account.signTransaction(unsignedTx);

      byte[] encodedTxBytes = Encoder.encodeToMsgPack(signedTx);
      String transactionId = algodClient.RawTransaction().rawtxn(encodedTxBytes).execute(txHeaders, txValues).body().txId;

      waitForConfirmation(transactionId);

      Long appId = getApplicationId(transactionId);

      return Optional.of(new BlockchainPoll(appId, poll.getName(), poll.getSender(), poll.getStartSubscriptionTime(), poll.getEndSubscriptionTime(),
          poll.getStartVotingTime(), poll.getEndVotingTime(), poll.getOptions(), poll.getMnemonicKey()));

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

    Transaction transaction;

    Long lastRound = getBlockChainLastRound();

    PollTealParams pollTealParams = pollBlockchainParamsAdapter
        .fromPollToPollTealParams(poll, lastRound);

    TEALProgram approvalProgram = tealProgramFactory.createApprovalProgramFrom(pollTealParams);
    TEALProgram clearStateProgram = tealProgramFactory.createClearStateProgram();

    try {
      transaction = Transaction.ApplicationCreateTransactionBuilder()
          .sender(poll.getSender())
          .args(arguments(pollTealParams))
          .suggestedParams(algodClient.TransactionParams().execute(headers, values).body())
          .approvalProgram(approvalProgram)
          .clearStateProgram(clearStateProgram)
          .globalStateSchema(new StateSchema(6, 1))
          .localStateSchema(new StateSchema(0, 1))
          .build();
    } catch (IllegalArgumentException e) {
      logger.error("Something goes wrong with Sender Address transaction", e);
      throw new InvalidSenderAddressException(e);
    } catch (Exception e) {
      logger.error("Something goes wrong getting blockchain parameters transaction", e);
      throw new BlockChainParameterException(e);
    }

    return transaction;
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

  private List<byte[]> arguments(PollTealParams pollTealParams) {

    return asList(pollTealParams.getStartSubscriptionTime(),
        pollTealParams.getEndSubscriptionTime(),
        pollTealParams.getStartVotingTime(),
        pollTealParams.getEndVotingTime());
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
