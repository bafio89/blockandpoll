package com.pollalgorand.rest;

import java.time.LocalDateTime;
import java.util.List;

public class Poll {

  public static final int NAME_MAXIMUM_LENGHT = 30;
  public static final int OPTION_MAXIMUM_LENGHT = 30;
  private String name;
  private LocalDateTime startSubscriptionTime;
  private LocalDateTime endSubscriptionTime;
  private LocalDateTime startVotingTime;
  private LocalDateTime endVotingTime;
  private List<String> options;
  private String sender;


  public Poll(String name, LocalDateTime startSubscriptionTime, LocalDateTime endSubscriptionTime,
      LocalDateTime startVotingTime,
      LocalDateTime endVotingTime,
      List<String> options,
      String sender) {
    this.name = name;
    this.startSubscriptionTime = startSubscriptionTime;
    this.endSubscriptionTime = endSubscriptionTime;
    this.startVotingTime = startVotingTime;
    this.endVotingTime = endVotingTime;
    this.options = options;
    this.sender = sender;

    validateName();
    validateOptions();
    validateDate();
  }

  private void validateOptions() {
    if(options.stream().anyMatch(option -> option.length() > OPTION_MAXIMUM_LENGHT)){
      throw new IllegalPollParameterException("at least an option name is too long");
    }
  }

  private void validateName() {
    if(name.length() > NAME_MAXIMUM_LENGHT){
      throw new IllegalPollParameterException("poll name is too long");
    }
  }

  private void validateDate() {
    if(startSubscriptionTime.isAfter(endSubscriptionTime)){
      throw new IllegalPollParameterException("start subscription date is after end subscription date");
    }

    if(startVotingTime.isAfter(endVotingTime)){
      throw new IllegalPollParameterException("start voting date is after end voting date");
    }

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
