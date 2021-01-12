package com.rockandpoll.rest.adapter.exceptions;

public class SendingTransactionException extends RuntimeException {

  public SendingTransactionException(String errorMessage) {
    super(String.format("Impossible to sign and send the transaction. %s", errorMessage));
  }
}
