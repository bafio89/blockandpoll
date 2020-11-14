package com.pollalgorand.rest;

import java.time.LocalDateTime;
import java.util.List;

public class Poll {

  private String name;
  private LocalDateTime startVotingTime;
  private LocalDateTime endVotingTime;
  private LocalDateTime startSubscriptionTime;
  private LocalDateTime endSubscriptionTime;
  private List<String> options;
  private String sender;

  public Poll() {

  }

  public Poll(String name, LocalDateTime startVotingTime, LocalDateTime endVotingTime,
      LocalDateTime startSubscriptionTime, LocalDateTime endSubscriptionTime, List<String> options,
      String sender) {
    this.name = name;
    this.startVotingTime = startVotingTime;
    this.endVotingTime = endVotingTime;
    this.startSubscriptionTime = startSubscriptionTime;
    this.endSubscriptionTime = endSubscriptionTime;
    this.options = options;
    this.sender = sender;
  }

  public String getName() {
    return name;
  }

  public LocalDateTime getStartVotingTime() {
    return startVotingTime;
  }

  public LocalDateTime getEndVotingTime() {
    return endVotingTime;
  }

  public LocalDateTime getStartSubscriptionTime() {
    return startSubscriptionTime;
  }

  public LocalDateTime getEndSubscriptionTime() {
    return endSubscriptionTime;
  }

  public String getSender() {
    return sender;
  }

  public List<String> getOptions() {
    return options;
  }
}
