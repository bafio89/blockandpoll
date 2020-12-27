package com.pollalgorand.rest.adapter.exceptions;

import java.security.NoSuchAlgorithmException;

public class SignTransactionException extends RuntimeException {

  public SignTransactionException(NoSuchAlgorithmException e) {
    super(String.format("Impossible to sign the transaction. %s", e.getMessage()), e);
  }
}
