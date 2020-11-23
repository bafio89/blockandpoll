package com.pollalgorand.rest;

public class CompileTealProgramException extends RuntimeException {

  public CompileTealProgramException(Exception e) {
    super("Something goes wrong during TEAL program compilation: " + e.getMessage(), e);
  }
}
