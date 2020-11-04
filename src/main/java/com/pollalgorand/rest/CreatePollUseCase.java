package com.pollalgorand.rest;


import java.util.Optional;

public class CreatePollUseCase {

  private final BlockChainPollRepository blockChainPollRepository;
  private final PollRepository postgresRepository;

  public CreatePollUseCase(BlockChainPollRepository blockChainPollRepository,
      PollRepository postgresRepository) {

    this.blockChainPollRepository = blockChainPollRepository;
    this.postgresRepository = postgresRepository;
  }

  public Optional<Poll> create(CreatePollRequest createPollRequest){

    Optional<Poll> poll = blockChainPollRepository.save(createPollRequest);

    poll.ifPresent(postgresRepository::save);

    return poll;
  }

}
