package com.pollalgorand.rest.adapter.exceptions;

public class SendingTransactionException extends RuntimeException {

  public SendingTransactionException(Exception e, String name) {
    super(String.format("Impossible to sign and send the transaction for poll with name %s. %s ", name, e.getMessage()), e);
  }
}
