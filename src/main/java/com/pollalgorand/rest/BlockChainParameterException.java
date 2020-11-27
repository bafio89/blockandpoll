package com.pollalgorand.rest;

public class BlockChainParameterException extends RuntimeException {

  public BlockChainParameterException(Exception e) {
    super("Something goes wrong building transaction: "+ e.getMessage(), e);
  }
}
