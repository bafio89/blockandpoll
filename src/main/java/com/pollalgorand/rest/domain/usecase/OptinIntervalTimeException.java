package com.pollalgorand.rest.domain.usecase;

public class OptinIntervalTimeException extends RuntimeException{

  public OptinIntervalTimeException(long appId) {
    super(String.format("Optin app %s is not open or expired", appId));
  }
}
