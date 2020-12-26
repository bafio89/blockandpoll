package com.pollalgorand.rest.adapter.exceptions;

public class InvalidMnemonicKeyException extends RuntimeException {

  public InvalidMnemonicKeyException(Exception e, String name) {
    super(String.format(
        "Impossible to create an account starting from mnemonic key for poll with name %s. %s",
        name, e.getMessage()), e);
  }
}
