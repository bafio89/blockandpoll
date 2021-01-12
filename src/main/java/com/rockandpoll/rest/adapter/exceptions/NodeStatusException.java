package com.rockandpoll.rest.adapter.exceptions;

public class NodeStatusException extends RuntimeException {

  public NodeStatusException(String message) {
    super("Something goes wrong getting node status: " + message);
  }
}
