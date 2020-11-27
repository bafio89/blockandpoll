package com.pollalgorand.rest;

public class NodeStatusException extends RuntimeException {

  public NodeStatusException(Exception e) {
    super("Something goes wrong getting node status: " + e.getMessage(), e);
  }
}
