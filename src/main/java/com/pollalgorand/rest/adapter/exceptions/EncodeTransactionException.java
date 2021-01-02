package com.pollalgorand.rest.adapter.exceptions;

public class EncodeTransactionException extends RuntimeException {

  public EncodeTransactionException(String errorMessage) {
    super(String.format("Impossible to encode the transaction. %s ", errorMessage));
  }
}
