package com.pollalgorand.rest;

import java.util.Objects;

public class Poll {

  private String name;

  public Poll() {
  }

  public Poll(String name) {
    this.name = name;
  }

  @Override
  public String toString() {
    return "Poll{" +
        "name='" + name + '\'' +
        '}';
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Poll poll = (Poll) o;
    return Objects.equals(name, poll.name);
  }

  @Override
  public int hashCode() {
    return Objects.hash(name);
  }
}
