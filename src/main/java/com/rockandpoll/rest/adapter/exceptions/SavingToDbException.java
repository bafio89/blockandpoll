package com.rockandpoll.rest.adapter.exceptions;

public class SavingToDbException extends RuntimeException {

  public SavingToDbException(String name, String message) {
    super(String.format("An error occours trying to save the poll with name:%s in the DB. Error message: %s.", name, message));
  }
}
