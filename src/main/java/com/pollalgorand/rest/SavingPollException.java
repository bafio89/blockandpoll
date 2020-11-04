package com.pollalgorand.rest;

public class SavingPollException extends RuntimeException {

  public SavingPollException() {
    super("Something goes wrong saving the new poll in the database");
  }
}
