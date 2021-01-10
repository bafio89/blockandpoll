package com.pollalgorand.rest.adapter.exceptions;

public class ApplicationNotFoundException extends
    RuntimeException {

  public ApplicationNotFoundException(long appId) {
    super(String.format("Impossible to find application with id: %s", appId));
  }
}
