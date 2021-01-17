package com.blockandpoll.rest.adapter;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class PollTealParams {

  private byte[] name;
  private byte[] startSubscriptionTime;
  private byte[] endSubscriptionTime;
  private byte[] startVotingTime;
  private byte[] endVotingTime;
  private List<String> options;
  private byte[] question;

  public PollTealParams() {
  }

  public PollTealParams(byte[] name, byte[] startSubscriptionTime, byte[] endSubscriptionTime,
      byte[] startVotingTime,
      byte[] endVotingTime,
      List<String> options, byte[] question) {
    this.name = name;
    this.startSubscriptionTime = startSubscriptionTime;
    this.endSubscriptionTime = endSubscriptionTime;
    this.startVotingTime = startVotingTime;
    this.endVotingTime = endVotingTime;
    this.options = options;
    this.question = question;
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

  public byte[] getQuestion() {
    return question;
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
        Arrays.equals(question, that.question);
  }

  @Override
  public int hashCode() {
    int result = Objects.hash(options);
    result = 31 * result + Arrays.hashCode(name);
    result = 31 * result + Arrays.hashCode(startSubscriptionTime);
    result = 31 * result + Arrays.hashCode(endSubscriptionTime);
    result = 31 * result + Arrays.hashCode(startVotingTime);
    result = 31 * result + Arrays.hashCode(endVotingTime);
    result = 31 * result + Arrays.hashCode(question);
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
        ", sender=" + Arrays.toString(question) +
        '}';
  }
}
