package com.pollalgorand.rest.domain;

import java.math.BigInteger;
import java.util.Map;
import java.util.Objects;

public class ApplicationInfoFromBlockchain {

  private final Map<String, BigInteger> optionsVotes;
  private final int size;

  public ApplicationInfoFromBlockchain(Map<String, BigInteger> optionsVotes, int size) {

    this.optionsVotes = optionsVotes;
    this.size = size;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ApplicationInfoFromBlockchain that = (ApplicationInfoFromBlockchain) o;
    return size == that.size &&
        Objects.equals(optionsVotes, that.optionsVotes);
  }

  @Override
  public int hashCode() {
    return Objects.hash(optionsVotes, size);
  }

  @Override
  public String toString() {
    return "ApplicationInfo{" +
        "optionsVotes=" + optionsVotes +
        ", size=" + size +
        '}';
  }
}
