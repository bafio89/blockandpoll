package com.pollalgorand.rest;


import com.algorand.algosdk.transaction.Transaction;
import java.util.Optional;

public class CreatePollUseCase {

  private final PollRepository blockChainPollRepository;
  private final PollRepository postgresRepository;

  public CreatePollUseCase(PollRepository blockChainPollRepository,
      PollRepository postgresRepository) {

    this.blockChainPollRepository = blockChainPollRepository;
    this.postgresRepository = postgresRepository;
  }

  public Optional<Poll> create(Poll poll){

    Optional<Poll> blockchainPoll = blockChainPollRepository.save(poll);

    blockchainPoll.ifPresent(postgresRepository::save);

    return blockchainPoll;
  }

  public Transaction createUnsignedTx(Poll poll) throws Exception {

    return blockChainPollRepository.createUnsignedTx(poll);
  }

}
