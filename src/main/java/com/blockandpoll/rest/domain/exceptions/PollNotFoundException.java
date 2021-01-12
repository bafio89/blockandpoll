package com.blockandpoll.rest.domain.exceptions;

public class PollNotFoundException extends RuntimeException {

  public PollNotFoundException(long appId) {
    super(String.format("Impossible to found the poll with appId: %s", appId));
  }
}
