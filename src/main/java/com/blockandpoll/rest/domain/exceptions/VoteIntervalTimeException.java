package com.blockandpoll.rest.domain.exceptions;

public class VoteIntervalTimeException extends RuntimeException {

  public VoteIntervalTimeException(long appId) {
    super(String.format("Vote interval time is not open or expired for appId %s", appId));
  }
}
