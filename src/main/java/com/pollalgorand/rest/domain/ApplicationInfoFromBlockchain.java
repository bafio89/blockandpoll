package com.pollalgorand.rest.domain;

import java.math.BigInteger;
import java.util.Map;
import java.util.Objects;

public class ApplicationInfoFromBlockchain {

  private Map<String, BigInteger> optionsVotes;
  private int subscribedAccountNumber;

  public ApplicationInfoFromBlockchain() {
  }

  public ApplicationInfoFromBlockchain(Map<String, BigInteger> optionsVotes, int subscribedAccountNumber) {

    this.optionsVotes = optionsVotes;
    this.subscribedAccountNumber = subscribedAccountNumber;
  }

  public Map<String, BigInteger> getOptionsVotes() {
    return optionsVotes;
  }

  public int getSubscribedAccountNumber() {
    return subscribedAccountNumber;
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
    return subscribedAccountNumber == that.subscribedAccountNumber &&
        Objects.equals(optionsVotes, that.optionsVotes);
  }

  @Override
  public int hashCode() {
    return Objects.hash(optionsVotes, subscribedAccountNumber);
  }

  @Override
  public String toString() {
    return "ApplicationInfo{" +
        "optionsVotes=" + optionsVotes +
        ", size=" + subscribedAccountNumber +
        '}';
  }
}
