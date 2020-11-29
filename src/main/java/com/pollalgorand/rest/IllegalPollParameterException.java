package com.pollalgorand.rest;

public class IllegalPollParameterException extends RuntimeException {

  public IllegalPollParameterException(String message) {
    super("Invalid poll parameters: " + message);
  }
}