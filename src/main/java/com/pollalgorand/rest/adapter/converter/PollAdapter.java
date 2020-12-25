package com.pollalgorand.rest.adapter.converter;

import com.pollalgorand.rest.domain.model.BlockchainPoll;
import com.pollalgorand.rest.domain.model.Poll;
import java.util.Optional;

public class PollAdapter {
  
  public Optional<BlockchainPoll> fromPollToBlockchainPoll(Poll poll, Long appId) {
    return Optional.of(new BlockchainPoll(appId, poll.getName(), poll.getSender(),
        poll.getStartSubscriptionTime(), poll.getEndSubscriptionTime(),
        poll.getStartVotingTime(), poll.getEndVotingTime(), poll.getOptions(),
        poll.getMnemonicKey(), poll.getDescription()));
  }
}