package com.blockandpoll.rest.adapter.exceptions;

public class OptinException extends RuntimeException {

  public OptinException(long appId, String address) {
    super(String.format("Something goes wrong sending transaction subscribing for app id %s from address", appId, address));
  }
}
