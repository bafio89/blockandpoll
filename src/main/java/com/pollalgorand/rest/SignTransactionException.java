package com.pollalgorand.rest;

import java.security.NoSuchAlgorithmException;

public class SignTransactionException extends RuntimeException {

  public SignTransactionException(NoSuchAlgorithmException e, String name) {
    super(String.format("Impossible to sign the transaction for poll with name %s. %s ", name, e.getMessage()), e);
  }
}
