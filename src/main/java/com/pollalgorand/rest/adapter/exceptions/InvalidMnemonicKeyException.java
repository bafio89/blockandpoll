package com.pollalgorand.rest.adapter.exceptions;

public class InvalidMnemonicKeyException extends RuntimeException {

  public InvalidMnemonicKeyException(Exception e) {
    super(String.format(
        "Impossible to create an account starting from mnemonic key. %s", e.getMessage()), e);
  }
}
