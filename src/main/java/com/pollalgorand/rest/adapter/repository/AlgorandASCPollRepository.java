package com.pollalgorand.rest.adapter.repository;

import static com.pollalgorand.rest.adapter.AlgorandUtils.txHeaders;
import static com.pollalgorand.rest.adapter.AlgorandUtils.txValues;

import com.algorand.algosdk.account.Account;
import com.algorand.algosdk.transaction.Transaction;
import com.algorand.algosdk.v2.client.common.AlgodClient;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.pollalgorand.rest.adapter.converter.PollBlockchainAdapter;
import com.pollalgorand.rest.adapter.exceptions.EncodeTransactionException;
import com.pollalgorand.rest.adapter.exceptions.InvalidMnemonicKeyException;
import com.pollalgorand.rest.adapter.exceptions.SendingTransactionException;
import com.pollalgorand.rest.adapter.exceptions.SignTransactionException;
import com.pollalgorand.rest.adapter.service.AccountCreatorService;
import com.pollalgorand.rest.adapter.service.AlgorandApplicationService;
import com.pollalgorand.rest.adapter.service.TransactionConfirmationService;
import com.pollalgorand.rest.adapter.service.TransactionSignerService;
import com.pollalgorand.rest.adapter.service.UnsignedASCTransactionService;
import com.pollalgorand.rest.domain.model.BlockchainPoll;
import com.pollalgorand.rest.domain.model.Poll;
import com.pollalgorand.rest.domain.repository.BlockchainPollRepository;
import java.security.NoSuchAlgorithmException;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AlgorandASCPollRepository implements BlockchainPollRepository {

  private Logger logger = LoggerFactory.getLogger(AlgorandASCPollRepository.class);

  private final AlgorandApplicationService algorandApplicationService;
  private final TransactionConfirmationService transactionConfirmationService;
  private AccountCreatorService accountCreatorService;
  private final TransactionSignerService transactionSignerService;
  private final PollBlockchainAdapter pollBlockchainAdapter;
  private final UnsignedASCTransactionService unsignedASCTransactionService;

  private AlgodClient algodClient;

  public AlgorandASCPollRepository(AlgodClient algodClient,
      AccountCreatorService accountCreatorService,
      TransactionSignerService transactionSignerService,
      UnsignedASCTransactionService unsignedASCTransactionService,
      PollBlockchainAdapter pollBlockchainAdapter,
      TransactionConfirmationService transactionConfirmationService,
      AlgorandApplicationService algorandApplicationService) {

    this.algodClient = algodClient;
    this.accountCreatorService = accountCreatorService;
    this.transactionSignerService = transactionSignerService;
    this.unsignedASCTransactionService = unsignedASCTransactionService;
    this.pollBlockchainAdapter = pollBlockchainAdapter;
    this.transactionConfirmationService = transactionConfirmationService;
    this.algorandApplicationService = algorandApplicationService;
  }

  @Override
  public Optional<BlockchainPoll> save(Poll poll) {

    Transaction unsignedTx = unsignedASCTransactionService.createUnsignedTxFor(poll);

    try {
      Account account = accountCreatorService.createAccountFrom(poll.getMnemonicKey());

      byte[] encodedTxBytes = transactionSignerService.sign(unsignedTx, account);

      String transactionId = algodClient.RawTransaction().rawtxn(encodedTxBytes).execute(txHeaders, txValues).body().txId;

      transactionConfirmationService.waitForConfirmation(transactionId);

      Long appId = algorandApplicationService.getApplicationId(transactionId);

      return pollBlockchainAdapter.fromPollToBlockchainPoll(poll, appId);

    } catch (NoSuchAlgorithmException e) {
      logger.error("Something goes wrong signing transaction", e);
      throw new SignTransactionException(e);
    } catch (IllegalArgumentException e) {
      logger.error("Something gone wrong creating account from mnemonic key creating poll {}.",poll, e);
      throw new InvalidMnemonicKeyException(e.getMessage());
    } catch (JsonProcessingException e) {
      logger.error("Something goes wrong encoding transaction", e);
      throw new EncodeTransactionException(e);
    } catch (Exception e) {
      logger.error("Something goes wrong sending transaction", e);
      throw new SendingTransactionException(e);
    }

  }

  @Override
  public Transaction createUnsignedTxFor(Poll poll) {

    return unsignedASCTransactionService.createUnsignedTxFor(poll);
  }
}
