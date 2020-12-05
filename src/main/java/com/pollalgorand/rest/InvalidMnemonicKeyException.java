package com.pollalgorand.rest;

import java.security.GeneralSecurityException;

public class InvalidMnemonicKeyException extends RuntimeException {

  public InvalidMnemonicKeyException(GeneralSecurityException e, String name) {
    super(String.format(
        "Impossible to create an account starting from mnemonic key for poll with name %s. %s",
        name, e.getMessage()), e);
  }
}
