package com.pollalgorand.rest;

import java.time.LocalDateTime;
import java.util.List;

public class PollEntity {

  private Long id;
  private String name;
  private LocalDateTime startSubscriptionTime;
  private LocalDateTime endSubscriptionTime;
  private LocalDateTime startVotingTime;
  private LocalDateTime endVotingTime;
  private List<String> options;
  private String sender;
  private String description;
  private Long appId;

  public PollEntity(Long id, String name, LocalDateTime startSubscriptionTime,
      LocalDateTime endSubscriptionTime, LocalDateTime startVotingTime,
      LocalDateTime endVotingTime, List<String> options, String sender,
      String description, Long appId) {
    this.id = id;
    this.name = name;
    this.startSubscriptionTime = startSubscriptionTime;
    this.endSubscriptionTime = endSubscriptionTime;
    this.startVotingTime = startVotingTime;
    this.endVotingTime = endVotingTime;
    this.options = options;
    this.sender = sender;
    this.description = description;
    this.appId = appId;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
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

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public Long getAppId() {
    return appId;
  }

  public void setAppId(Long appId) {
    this.appId = appId;
  }
}
