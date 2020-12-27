package com.pollalgorand.rest.domain.exceptions;

public class OptinAlreadDoneException extends RuntimeException {

  public OptinAlreadDoneException(long appId) {
    super(String.format("It seems that optin has been already done for app %s", appId));
  }
}
