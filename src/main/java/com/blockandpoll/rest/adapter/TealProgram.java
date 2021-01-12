package com.blockandpoll.rest.adapter;

import java.util.Arrays;

public class TealProgram {

  private byte[] program = null;

  public TealProgram(byte[] program) {
    this.program = program;
  }

  public byte[] getProgram() {
    return program;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    TealProgram that = (TealProgram) o;
    return Arrays.equals(program, that.program);
  }

  @Override
  public int hashCode() {
    return Arrays.hashCode(program);
  }
}
