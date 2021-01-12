package com.rockandpoll.rest.adapter.exceptions;

public class PastDateException extends RuntimeException{

  public PastDateException(String message) {
    super(message);
  }
}
