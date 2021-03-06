package com.blockandpoll.rest.web.request;

import java.time.LocalDateTime;
import java.util.List;

public class PollRequest {

  private String name;
  private LocalDateTime startSubscriptionTime;
  private LocalDateTime endSubscriptionTime;
  private LocalDateTime startVotingTime;
  private LocalDateTime endVotingTime;
  private List<String> options;
  private String question;
  private String mnemonicKey;
  private String description;

  public PollRequest() {
  }

  public PollRequest(String name, LocalDateTime startSubscriptionTime,
      LocalDateTime endSubscriptionTime, LocalDateTime startVotingTime,
      LocalDateTime endVotingTime, List<String> options, String question,
      String mnemonicKey, String description) {
    this.name = name;
    this.startSubscriptionTime = startSubscriptionTime;
    this.endSubscriptionTime = endSubscriptionTime;
    this.startVotingTime = startVotingTime;
    this.endVotingTime = endVotingTime;
    this.options = options;
    this.question = question;
    this.mnemonicKey = mnemonicKey;
    this.description = description;
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

  public String getQuestion() {
    return question;
  }

  public void setQuestion(String question) {
    this.question = question;
  }

  public String getMnemonicKey() {
    return mnemonicKey;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  @Override
  public String toString() {
    return "PollRequest{" +
        "name='" + name + '\'' +
        ", startSubscriptionTime=" + startSubscriptionTime +
        ", endSubscriptionTime=" + endSubscriptionTime +
        ", startVotingTime=" + startVotingTime +
        ", endVotingTime=" + endVotingTime +
        ", options=" + options +
        ", sender='" + question + '\'' +
        ", mnemonicKey='" + mnemonicKey + '\'' +
        ", description='" + description + '\'' +
        '}';
  }
}
