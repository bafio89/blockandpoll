package com.pollalgorand.rest.domain.usecase;

public class OptinExpiredException extends RuntimeException{

  public OptinExpiredException(long appId) {
    super(String.format("Optin app %s is expired", appId));
  }
}
