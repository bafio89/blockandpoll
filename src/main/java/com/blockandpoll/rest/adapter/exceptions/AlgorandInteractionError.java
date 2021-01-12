package com.blockandpoll.rest.adapter.exceptions;

public class AlgorandInteractionError extends RuntimeException {

  public AlgorandInteractionError(String message) {
    super(String.format("An error occurs calling algorand blockchain. %s", message));
  }
  public AlgorandInteractionError(int errorCode, String message) {
    super(String.format("An error occurs calling algorand blockchain. Response has code %s. Error message: %s", errorCode, message));
  }
}
