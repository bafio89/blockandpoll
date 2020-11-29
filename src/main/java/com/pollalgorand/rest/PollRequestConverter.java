package com.pollalgorand.rest;

public class PollRequestConverter {

  public Poll fromRequestToDomain(PollRequest poll) {
    return new Poll(poll.getName(),
        poll.getStartSubscriptionTime(),
        poll.getStartSubscriptionTime(),
        poll.getStartVotingTime(),
        poll.getEndVotingTime(),
        poll.getOptions(),
        poll.getSender());
  }
}
