package com.pollalgorand.rest.adapter.exceptions;

import com.fasterxml.jackson.core.JsonProcessingException;

public class EncodeTransactionException extends RuntimeException {

  public EncodeTransactionException(JsonProcessingException e) {
    super(String.format("Impossible to encode the transaction. %s ", e.getMessage()), e);
  }
}
