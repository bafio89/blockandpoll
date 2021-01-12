package com.blockandpoll.rest.adapter.exceptions;

public class VoteException extends RuntimeException {

  public VoteException(long appId, String address) {
    super(String.format("Something goes wrong sending vote transaction for app id %s from address %s", appId, address));
  }
}
