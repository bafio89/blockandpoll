package com.pollalgorand.rest.domain.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.pollalgorand.rest.domain.exceptions.IllegalPollParameterException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

public class Poll {

  public static final int NAME_MAXIMUM_LENGHT = 30;
  public static final int OPTION_MAXIMUM_LENGHT = 30;
  private String name;

  @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
  private LocalDateTime startSubscriptionTime;
  @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
  private LocalDateTime endSubscriptionTime;
  @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
  private LocalDateTime startVotingTime;
  @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
  private LocalDateTime endVotingTime;
  private List<String> options;
  private String question;
  @JsonIgnore
  private String mnemonicKey;
  private String description;

  public Poll() {
  }

  public Poll(String name, LocalDateTime startSubscriptionTime, LocalDateTime endSubscriptionTime,
      LocalDateTime startVotingTime,
      LocalDateTime endVotingTime,
      List<String> options,
      String question,
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

    validateName();
    validateOptions();
    validateDate();
  }

  @JsonProperty
  public String pollStatus(){
    LocalDateTime now = LocalDateTime.now();
    if(now.isAfter(startVotingTime) && now.isBefore(endVotingTime)){
       return "Open";
    }
    if(now.isAfter(startSubscriptionTime) && now.isBefore(endSubscriptionTime)){
      return "Subscription open";
    }
    if(now.isAfter(endVotingTime)){
      return "Expired";
    }

    return "Not yet open";
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

  public String getQuestion() {
    return question;
  }

  public List<String> getOptions() {
    return options;
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
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Poll poll = (Poll) o;
    return Objects.equals(name, poll.name) &&
        Objects.equals(startSubscriptionTime, poll.startSubscriptionTime) &&
        Objects.equals(endSubscriptionTime, poll.endSubscriptionTime) &&
        Objects.equals(startVotingTime, poll.startVotingTime) &&
        Objects.equals(endVotingTime, poll.endVotingTime) &&
        Objects.equals(options, poll.options) &&
        Objects.equals(question, poll.question);
  }

  @Override
  public int hashCode() {
    return Objects
        .hash(name, startSubscriptionTime, endSubscriptionTime, startVotingTime, endVotingTime,
            options, question);
  }

  @Override
  public String toString() {
    return "Poll{" +
        "name='" + name + '\'' +
        ", startSubscriptionTime=" + startSubscriptionTime +
        ", endSubscriptionTime=" + endSubscriptionTime +
        ", startVotingTime=" + startVotingTime +
        ", endVotingTime=" + endVotingTime +
        ", options=" + options +
        ", question='" + question + '\'' +
        '}';
  }
}
