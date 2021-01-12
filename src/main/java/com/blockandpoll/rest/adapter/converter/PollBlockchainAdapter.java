package com.blockandpoll.rest.adapter.converter;

import static java.nio.charset.StandardCharsets.UTF_8;

import com.blockandpoll.rest.adapter.PollTealParams;
import com.blockandpoll.rest.domain.model.BlockchainPoll;
import com.blockandpoll.rest.domain.model.Poll;
import java.nio.ByteBuffer;

public class PollBlockchainAdapter {

  private AlgorandDateAdapter algorandDateAdapter;

  public PollBlockchainAdapter(AlgorandDateAdapter algorandDateAdapter) {

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
        poll.getQuestion().getBytes(UTF_8));
  }

  private byte[] convertLongToByteArray(long value) {
    return ByteBuffer.allocate(8).putLong(value).array();
  }

  public BlockchainPoll fromPollToBlockchainPoll(Poll poll, Long appId) {
    return new BlockchainPoll(appId, poll.getName(), poll.getQuestion(),
        poll.getStartSubscriptionTime(), poll.getEndSubscriptionTime(),
        poll.getStartVotingTime(), poll.getEndVotingTime(), poll.getOptions(),
        poll.getMnemonicKey(), poll.getDescription());
  }
}
