package com.pollalgorand.rest.adapter.exceptions;

import com.fasterxml.jackson.core.JsonProcessingException;

public class EncodeTransactionException extends RuntimeException {

  public EncodeTransactionException(JsonProcessingException e, String name) {
    super(String.format("Impossible to encode the transaction for poll with name %s. %s ", name, e.getMessage()), e);
  }
}
