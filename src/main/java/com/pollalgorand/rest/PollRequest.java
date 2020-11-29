package com.pollalgorand.rest;

import java.time.LocalDateTime;
import java.util.List;

public class PollRequest {

  private String name;
  private LocalDateTime startSubscriptionTime;
  private LocalDateTime endSubscriptionTime;
  private LocalDateTime startVotingTime;
  private LocalDateTime endVotingTime;
  private List<String> options;
  private String sender;

  public PollRequest() {
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public LocalDateTime getStartSubscriptionTime() {
    return startSubscriptionTime;
  }

  public void setStartSubscriptionTime(LocalDateTime startSubscriptionTime) {
    this.startSubscriptionTime = startSubscriptionTime;
  }

  public LocalDateTime getEndSubscriptionTime() {
    return endSubscriptionTime;
  }

  public void setEndSubscriptionTime(LocalDateTime endSubscriptionTime) {
    this.endSubscriptionTime = endSubscriptionTime;
  }

  public LocalDateTime getStartVotingTime() {
    return startVotingTime;
  }

  public void setStartVotingTime(LocalDateTime startVotingTime) {
    this.startVotingTime = startVotingTime;
  }

  public LocalDateTime getEndVotingTime() {
    return endVotingTime;
  }

  public void setEndVotingTime(LocalDateTime endVotingTime) {
    this.endVotingTime = endVotingTime;
  }

  public List<String> getOptions() {
    return options;
  }

  public void setOptions(List<String> options) {
    this.options = options;
  }

  public String getSender() {
    return sender;
  }

  public void setSender(String sender) {
    this.sender = sender;
  }
}
