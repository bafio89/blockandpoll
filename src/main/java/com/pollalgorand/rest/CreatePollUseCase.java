package com.pollalgorand.rest;


import java.util.Optional;
import java.util.function.Function;

public class CreatePollUseCase {

  private final BlockChainRepository blockChainRepository;
  private final PollRepository postgresRepository;

  public CreatePollUseCase(BlockChainRepository blockChainRepository,
      PollRepository postgresRepository) {

    this.blockChainRepository = blockChainRepository;
    this.postgresRepository = postgresRepository;
  }

  public Optional<Poll> create(CreatePollRequest createPollRequest){

    Optional<Poll> poll = blockChainRepository.save(createPollRequest);

    poll.ifPresent(postgresRepository::save);

    return poll;
  }

}
