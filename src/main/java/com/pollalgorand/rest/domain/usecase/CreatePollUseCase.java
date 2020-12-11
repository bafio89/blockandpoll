package com.pollalgorand.rest.domain.usecase;


import com.algorand.algosdk.transaction.Transaction;
import com.pollalgorand.rest.domain.model.Poll;
import com.pollalgorand.rest.domain.repository.PollRepository;
import java.util.Optional;

public class CreatePollUseCase {

  private final PollRepository blockChainPollRepository;
  private final PollRepository postgresRepository;

  public CreatePollUseCase(PollRepository blockChainPollRepository,
      PollRepository postgresRepository) {

    this.blockChainPollRepository = blockChainPollRepository;
    this.postgresRepository = postgresRepository;
  }

  public Optional<Poll> create(Poll poll) {

    Optional<Poll> blockchainPoll = blockChainPollRepository.save(poll);

//    blockchainPoll.ifPresent(postgresRepository::save);

    return blockchainPoll;
  }

  public Transaction createUnsignedTx(Poll poll) {

    return blockChainPollRepository.createUnsignedTxFor(poll);
  }

}
