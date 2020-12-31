package com.pollalgorand.rest.adapter.repository;

import com.algorand.algosdk.account.Account;
import com.algorand.algosdk.transaction.Transaction;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.pollalgorand.rest.adapter.exceptions.EncodeTransactionException;
import com.pollalgorand.rest.adapter.exceptions.SendingTransactionException;
import com.pollalgorand.rest.adapter.exceptions.SignTransactionException;
import com.pollalgorand.rest.adapter.service.AccountCreatorService;
import com.pollalgorand.rest.adapter.service.BuildOptinTransactionService;
import com.pollalgorand.rest.adapter.service.TransactionConfirmationService;
import com.pollalgorand.rest.adapter.service.TransactionSenderService;
import com.pollalgorand.rest.adapter.service.TransactionSignerService;
import com.pollalgorand.rest.domain.repository.BlockchainWriteRepository;
import com.pollalgorand.rest.domain.request.OptinAppRequest;
import com.pollalgorand.rest.domain.request.VoteAppRequest;
import java.security.NoSuchAlgorithmException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AlgorandWriteRepository implements BlockchainWriteRepository {

  private Logger logger = LoggerFactory.getLogger(AlgorandASCPollRepository.class);

  private final AccountCreatorService accountCreator;
  private final BuildOptinTransactionService buildOptinTransactionService;
  private final TransactionSignerService transactionSignerService;
  private TransactionSenderService transactionSenderService;
  private final TransactionConfirmationService transactionConfirmationService;

  public AlgorandWriteRepository(AccountCreatorService accountCreator,
      BuildOptinTransactionService buildOptinTransactionService,
      TransactionSignerService transactionSignerService,
      TransactionSenderService transactionSenderService,
      TransactionConfirmationService transactionConfirmationService) {

    this.accountCreator = accountCreator;
    this.buildOptinTransactionService = buildOptinTransactionService;
    this.transactionSignerService = transactionSignerService;
    this.transactionSenderService = transactionSenderService;
    this.transactionConfirmationService = transactionConfirmationService;
  }

  @Override
  public void optin(OptinAppRequest optinAppRequest) {

    try {
      Account account = optinAppRequest.getAccount();

      Transaction unsignedTransaction = buildOptinTransactionService
          .buildTransaction(account, optinAppRequest);

      byte[] signedTxBytes = transactionSignerService.sign(unsignedTransaction, account);

      String transactionId = transactionSenderService.send(signedTxBytes);

      transactionConfirmationService.waitForConfirmation(transactionId);

    } catch (NoSuchAlgorithmException e) {
      logger.error("Something goes wrong signing transaction subscribing for app id {}",
          optinAppRequest.getAppId(), e);
      throw new SignTransactionException(e);
    } catch (JsonProcessingException e) {
      logger.error("Something goes wrong encoding transaction subscribing for app id {}",
          optinAppRequest.getAppId(), e);
      throw new EncodeTransactionException(e);
    } catch (Exception e) {
      logger.error("Something goes wrong sending transaction subscribing for app id {}", optinAppRequest.getAppId(), e);
      throw new SendingTransactionException(e);
    }

  }

  @Override
  public void vote(VoteAppRequest voteAppRequest) {

  }
}
