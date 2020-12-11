package com.pollalgorand.rest.domain.exceptions;

public class IllegalPollParameterException extends RuntimeException {

  public IllegalPollParameterException(String message) {
    super("Invalid poll parameters: " + message);
  }
}
