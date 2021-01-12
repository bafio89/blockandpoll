package com.blockandpoll.rest.adapter.exceptions;

public class RetrievingApplicationIdException extends RuntimeException {

  public RetrievingApplicationIdException(Exception e, String transactionId) {
    super(String.format("Impossible to get Application id for transaction: %s. Error message: %s", transactionId, e.getMessage()), e);
  }
}
