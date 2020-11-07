package com.pollalgorand.rest;

import java.util.Date;
import java.util.List;

public class Poll {

  private String name;
  private Date startVotingTime;
  private Date endVotingTime;
  private Date startSubscriptionTime;
  private Date endSubscriptionTime;
  private List<String> options;
  private String sender;

  public Poll() {

  }

  public Poll(String name, Date startVotingTime, Date endVotingTime,
      Date startSubscriptionTime, Date endSubscriptionTime, List<String> options,
      String sender) {
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
}
