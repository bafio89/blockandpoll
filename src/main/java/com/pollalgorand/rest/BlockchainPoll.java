package com.pollalgorand.rest;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

public class BlockchainPoll extends Poll {

  private String transactionId;

  public BlockchainPoll(String transactionId, String name, String sender,
      LocalDateTime startSubscriptionTime, LocalDateTime endSubscriptionTime,
      LocalDateTime startVotingTime, LocalDateTime endVotingTime,
      List<String> options, String mnemonicKey) {
    super(name,startSubscriptionTime,endSubscriptionTime,startVotingTime,endVotingTime,options,sender, mnemonicKey);
    this.transactionId = transactionId;
  }

  @Override
  public String toString() {
    return "Poll{" +
        "name='" + transactionId + '\'' +
        '}';
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    BlockchainPoll blockchainPoll = (BlockchainPoll) o;
    return Objects.equals(transactionId, blockchainPoll.transactionId);
  }

  @Override
  public int hashCode() {
    return Objects.hash(transactionId);
  }
}
