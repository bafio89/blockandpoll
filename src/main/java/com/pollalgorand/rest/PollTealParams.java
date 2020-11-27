package com.pollalgorand.rest;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class PollTealParams {

  private final byte[] name;
  private final byte[] startSubscriptionTime;
  private final byte[] endSubscriptionTime;
  private final byte[] startVotingTime;
  private final byte[] endVotingTime;
  private final List<String> options;
  private final byte[] sender;

  public PollTealParams(byte[] name, byte[] startSubscriptionTime, byte[] endSubscriptionTime,
      byte[] startVotingTime,
      byte[] endVotingTime,
      List<String> options, byte[] sender) {
    this.name = name;
    this.startSubscriptionTime = startSubscriptionTime;
    this.endSubscriptionTime = endSubscriptionTime;
    this.startVotingTime = startVotingTime;
    this.endVotingTime = endVotingTime;
    this.options = options;
    this.sender = sender;
  }

  public byte[] getStartSubscriptionTime() {
    return startSubscriptionTime;
  }

  public byte[] getEndSubscriptionTime() {
    return endSubscriptionTime;
  }

  public byte[] getStartVotingTime() {
    return startVotingTime;
  }

  public byte[] getEndVotingTime() {
    return endVotingTime;
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
        Arrays.equals(startSubscriptionTime, that.startSubscriptionTime) &&
        Arrays.equals(endSubscriptionTime, that.endSubscriptionTime) &&
        Arrays.equals(startVotingTime, that.startVotingTime) &&
        Arrays.equals(endVotingTime, that.endVotingTime) &&
        Objects.equals(options, that.options) &&
        Arrays.equals(sender, that.sender);
  }

  @Override
  public int hashCode() {
    int result = Objects.hash(options);
    result = 31 * result + Arrays.hashCode(name);
    result = 31 * result + Arrays.hashCode(startSubscriptionTime);
    result = 31 * result + Arrays.hashCode(endSubscriptionTime);
    result = 31 * result + Arrays.hashCode(startVotingTime);
    result = 31 * result + Arrays.hashCode(endVotingTime);
    result = 31 * result + Arrays.hashCode(sender);
    return result;
  }

  @Override
  public String toString() {
    return "PollTealParams{" +
        "name=" + Arrays.toString(name) +
        ", startSubscriptionTime=" + Arrays.toString(startSubscriptionTime) +
        ", endSubscriptionTime=" + Arrays.toString(endSubscriptionTime) +
        ", startVotingTime=" + Arrays.toString(startVotingTime) +
        ", endVotingTime=" + Arrays.toString(endVotingTime) +
        ", options=" + options +
        ", sender=" + Arrays.toString(sender) +
        '}';
  }
}
