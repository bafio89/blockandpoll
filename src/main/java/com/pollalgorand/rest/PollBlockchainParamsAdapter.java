package com.pollalgorand.rest;

import static java.nio.charset.StandardCharsets.UTF_8;
import static java.util.stream.Collectors.toList;

import java.util.List;

public class PollBlockchainParamsAdapter {

  public PollTealParams fromPollToPollTealParams(Poll poll) {

    List<byte[]> optionsInBytes = poll.getOptions().stream().map(option -> option.getBytes(UTF_8))
        .collect(toList());

    return new PollTealParams(poll.getName().getBytes(UTF_8), poll.getStartVotingTime(),
        poll.getEndVotingTime(),
        poll.getStartSubscriptionTime(), poll.getEndSubscriptionTime(), optionsInBytes,
        poll.getSender().getBytes(UTF_8));
  }
}
