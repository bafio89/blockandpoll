package com.pollalgorand.rest;

import com.algorand.algosdk.algod.client.api.AlgodApi;
import java.util.Optional;

public class AlgorandPollRepository implements BlockChainPollRepository {

  private AlgodApi algodApi;
  private String srcAccount;

  public AlgorandPollRepository(AlgodApi algodApi, String srcAccount) {

    this.algodApi = algodApi;
    this.srcAccount = srcAccount;
  }

  @Override
  public Optional<Poll> save(CreatePollRequest createPollRequest) {
    return Optional.empty();
  }
}
