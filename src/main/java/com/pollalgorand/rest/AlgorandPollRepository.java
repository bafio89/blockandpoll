package com.pollalgorand.rest;

import com.algorand.algosdk.v2.client.common.AlgodClient;
import java.util.Optional;

public class AlgorandPollRepository implements PollRepository {

  private AlgodClient algodClient;
  private String srcAccount;

  public AlgorandPollRepository(AlgodClient algodClient, String srcAccount) {

    this.algodClient = algodClient;
    this.srcAccount = srcAccount;
  }

  @Override
  public Optional<Poll> save(Poll poll) {
    return Optional.empty();
  }
}
