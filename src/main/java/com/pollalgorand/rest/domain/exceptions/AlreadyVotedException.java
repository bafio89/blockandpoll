package com.pollalgorand.rest.domain.exceptions;

public class AlreadyVotedException extends RuntimeException {

  public AlreadyVotedException(String address, long appId) {
    super(String.format("Address %s have already voted for appId %s",address, appId));
  }
}
