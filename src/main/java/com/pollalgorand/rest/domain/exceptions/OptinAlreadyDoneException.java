package com.pollalgorand.rest.domain.exceptions;

public class OptinAlreadyDoneException extends RuntimeException {

  public OptinAlreadyDoneException(long appId) {
    super(String.format("It seems that optin has been already done for app %s", appId));
  }
}
