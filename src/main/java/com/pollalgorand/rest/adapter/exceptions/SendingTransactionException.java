package com.pollalgorand.rest.adapter.exceptions;

public class SendingTransactionException extends RuntimeException {

  public SendingTransactionException(Exception e) {
    super(String.format("Impossible to sign and send the transaction. %s", e.getMessage()), e);
  }
}
