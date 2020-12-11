package com.pollalgorand.rest.adapter.exceptions;

public class SavingToDbException extends RuntimeException {

  public SavingToDbException(String name, Exception e) {
    super(String.format("An error occours trying to save the poll with name:%s in the DB. Error: ", name, e));
  }
}
