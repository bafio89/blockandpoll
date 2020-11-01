package com.pollalgorand.rest;

import java.util.Date;
import java.util.List;

public class CreatePollRequest {

  private String name;
  private Date startVotingTime;
  private Date endVotingTime;
  private Date startSubscriptionTime;
  private Date endSubscriptionTime;
  private List<String> options;

  public CreatePollRequest(String name, Date startVotingTime, Date endVotingTime,
      Date startSubscriptionTime, Date endSubscriptionTime, List<String> options) {
    this.name = name;
    this.startVotingTime = startVotingTime;
    this.endVotingTime = endVotingTime;
    this.startSubscriptionTime = startSubscriptionTime;
    this.endSubscriptionTime = endSubscriptionTime;
    this.options = options;
  }
}
