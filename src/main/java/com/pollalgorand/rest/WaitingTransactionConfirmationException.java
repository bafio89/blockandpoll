package com.pollalgorand.rest;

public class WaitingTransactionConfirmationException extends RuntimeException {

  public WaitingTransactionConfirmationException(Exception e, String name) {
    super(String.format("Impossible to wait the transaction confirmation for poll with name %s. %s ", name, e.getMessage()), e);
  }
}
