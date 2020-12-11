package com.pollalgorand.rest.domain.model;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

public class BlockchainPoll extends Poll {

  private Long appId;

  public BlockchainPoll(Long appId, String name, String sender,
      LocalDateTime startSubscriptionTime, LocalDateTime endSubscriptionTime,
      LocalDateTime startVotingTime, LocalDateTime endVotingTime,
      List<String> options, String mnemonicKey, String description) {
    super(name,startSubscriptionTime,endSubscriptionTime,startVotingTime,endVotingTime,options,sender, mnemonicKey,
        description);
    this.appId = appId;
  }

  public Long getAppId() {
    return appId;
  }

  public void setAppId(Long appId) {
    this.appId = appId;
  }

  @Override
  public String toString() {
    return "BlockchainPoll{" +
        "appId=" + appId +
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
    if (!super.equals(o)) {
      return false;
    }
    BlockchainPoll that = (BlockchainPoll) o;
    return Objects.equals(appId, that.appId);
  }

  @Override
  public int hashCode() {
    return Objects.hash(super.hashCode(), appId);
  }
}
