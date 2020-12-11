package com.pollalgorand.rest.adapter.exceptions;

public class InvalidSenderAddressException extends RuntimeException {

  public InvalidSenderAddressException(Exception e) {
    super("Something went wrong with sender address during transaction creation: " + e.getMessage());
  }
}
