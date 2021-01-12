package com.rockandpoll.rest.domain.model;

import java.util.Objects;

public class EnrichedBlockchainPoll {

  private BlockchainPoll blockchainPoll;
  private ApplicationInfoFromBlockchain optionsVotes;

  public EnrichedBlockchainPoll() {
  }

  public EnrichedBlockchainPoll(BlockchainPoll blockchainPoll,
      ApplicationInfoFromBlockchain optionsVotes) {

    this.blockchainPoll = blockchainPoll;
    this.optionsVotes = optionsVotes;
  }

  public BlockchainPoll getBlockchainPoll() {
    return blockchainPoll;
  }

  public ApplicationInfoFromBlockchain getOptionsVotes() {
    return optionsVotes;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    EnrichedBlockchainPoll that = (EnrichedBlockchainPoll) o;
    return Objects.equals(blockchainPoll, that.blockchainPoll) &&
        Objects.equals(optionsVotes, that.optionsVotes);
  }

  @Override
  public int hashCode() {
    return Objects.hash(blockchainPoll, optionsVotes);
  }

  @Override
  public String toString() {
    return "EnrichedBlockchainPoll{" +
        "blockchainPoll=" + blockchainPoll +
        ", optionsVotes=" + optionsVotes +
        '}';
  }
}
