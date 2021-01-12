package com.rockandpoll.rest.adapter.service;

import com.algorand.algosdk.account.Account;
import com.algorand.algosdk.transaction.Transaction;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.rockandpoll.rest.adapter.exceptions.EncodeTransactionException;
import com.rockandpoll.rest.adapter.exceptions.SendingTransactionException;
import com.rockandpoll.rest.adapter.exceptions.SignTransactionException;
import java.security.NoSuchAlgorithmException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TransactionWriterService {

  private Logger logger = LoggerFactory.getLogger(TransactionWriterService.class);

  private final TransactionSignerService transactionSignerService;
  private final TransactionSenderService transactionSenderService;
  private final TransactionConfirmationService transactionConfirmationService;

  public TransactionWriterService(TransactionSignerService transactionSignerService,
      TransactionSenderService transactionSenderService,
      TransactionConfirmationService transactionConfirmationService) {

    this.transactionSignerService = transactionSignerService;
    this.transactionSenderService = transactionSenderService;
    this.transactionConfirmationService = transactionConfirmationService;
  }

  public String write(Account account, Transaction unsignedTransaction) {

    try {

      byte[] signedTxBytes = transactionSignerService.sign(unsignedTransaction, account);

      String transactionId = transactionSenderService.send(signedTxBytes);

      transactionConfirmationService.waitForConfirmation(transactionId);

      return transactionId;

    } catch (JsonProcessingException e) {
      logger.error("Something goes wrong encoding transaction for account with address {}",
          account.getAddress().toString(), e);
      throw new EncodeTransactionException(e.getMessage());
    } catch (NoSuchAlgorithmException e) {
      logger.error("Something goes wrong signing transaction for account with address {}",
          account.getAddress().toString(), e);
      throw new SignTransactionException(e.getMessage());
    }  catch (Exception e) {
      logger.error("Something goes wrong sending transaction for account with address {}", account.getAddress().toString(), e);
      throw new SendingTransactionException(e.getMessage());
    }
  }
}
