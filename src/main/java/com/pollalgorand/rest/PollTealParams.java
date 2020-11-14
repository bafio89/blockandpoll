package com.pollalgorand.rest;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class PollTealParams {

  private final byte[] name;
  private final Long startVotingTime;
  private final Long endVotingTime;
  private final Long startSubscriptionTime;
  private final Long endSubscriptionTime;
  private final List<String> options;
  private final byte[] sender;

  public PollTealParams(byte[] name, Long startVotingTime, Long endVotingTime,
      Long startSubscriptionTime, Long endSubscriptionTime,
      List<String> options, byte[] sender) {
    this.name = name;
    this.startVotingTime = startVotingTime;
    this.endVotingTime = endVotingTime;
    this.startSubscriptionTime = startSubscriptionTime;
    this.endSubscriptionTime = endSubscriptionTime;
    this.options = options;
    this.sender = sender;
  }

  public List<String> getOptions() {
    return options;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    PollTealParams that = (PollTealParams) o;
    return Arrays.equals(name, that.name) &&
        Objects.equals(startVotingTime, that.startVotingTime) &&
        Objects.equals(endVotingTime, that.endVotingTime) &&
        Objects.equals(startSubscriptionTime, that.startSubscriptionTime) &&
        Objects.equals(endSubscriptionTime, that.endSubscriptionTime) &&
        Arrays.equals(sender, that.sender);
  }

  @Override
  public int hashCode() {
    int result = Objects
        .hash(startVotingTime, endVotingTime, startSubscriptionTime, endSubscriptionTime,
            options);
    result = 31 * result + Arrays.hashCode(name);
    result = 31 * result + Arrays.hashCode(sender);
    return result;
  }

  @Override
  public String toString() {
    return "PollTealParams{" +
        "name=" + Arrays.toString(name) +
        ", startVotingTime=" + startVotingTime +
        ", endVotingTime=" + endVotingTime +
        ", startSubscriptionTime=" + startSubscriptionTime +
        ", endSubscriptionTime=" + endSubscriptionTime +
        ", optionsInByte=" + options +
        ", sender=" + Arrays.toString(sender) +
        '}';
  }
}
