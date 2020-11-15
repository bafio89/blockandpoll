package com.pollalgorand.rest;

public class PastDateException extends RuntimeException{

  public PastDateException(String message) {
    super(message);
  }
}
