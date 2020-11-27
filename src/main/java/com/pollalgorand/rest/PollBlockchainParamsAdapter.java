package com.pollalgorand.rest;

import static java.nio.charset.StandardCharsets.UTF_8;

import java.nio.ByteBuffer;

public class PollBlockchainParamsAdapter {

  private AlgorandDateAdapter algorandDateAdapter;

  public PollBlockchainParamsAdapter(AlgorandDateAdapter algorandDateAdapter) {

    this.algorandDateAdapter = algorandDateAdapter;
  }

  public PollTealParams fromPollToPollTealParams(Poll poll, Long lastRound) {

    Long startSubscriptionBlockNumber = algorandDateAdapter
        .fromDateToBlockNumber(poll.getStartSubscriptionTime(), lastRound);
    Long endSubscriptionBlockNumber = algorandDateAdapter
        .fromDateToBlockNumber(poll.getEndSubscriptionTime(), lastRound);
    Long startVotingBlockNumber = algorandDateAdapter.fromDateToBlockNumber(poll.getStartVotingTime(), lastRound);
    Long endVotingBlockNumber = algorandDateAdapter.fromDateToBlockNumber(poll.getEndVotingTime(), lastRound);

    return new PollTealParams(poll.getName().getBytes(UTF_8),
        convertLongToByteArray(startSubscriptionBlockNumber),
        convertLongToByteArray(endSubscriptionBlockNumber),
        convertLongToByteArray(startVotingBlockNumber),
        convertLongToByteArray(endVotingBlockNumber),
        poll.getOptions(),
        poll.getSender().getBytes(UTF_8));
  }

  public byte[] convertLongToByteArray(long value) {
    return ByteBuffer.allocate(8).putLong(value).array();
  }
}
