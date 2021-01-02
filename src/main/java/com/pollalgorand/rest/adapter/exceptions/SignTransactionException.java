package com.pollalgorand.rest.adapter.exceptions;

public class SignTransactionException extends RuntimeException {

  public SignTransactionException(String errorMessage) {
    super(String.format("Impossible to sign the transaction. %s", errorMessage));
  }
}
