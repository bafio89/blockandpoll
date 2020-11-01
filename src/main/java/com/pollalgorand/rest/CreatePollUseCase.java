package com.pollalgorand.rest;


public class CreatePollUseCase {

  private final BlockChainRepository blockChainRepository;
  private final PollRepository postgresRepository;

  public CreatePollUseCase(BlockChainRepository blockChainRepository,
      PollRepository postgresRepository) {

    this.blockChainRepository = blockChainRepository;
    this.postgresRepository = postgresRepository;
  }

  public Poll create(CreatePollRequest createPollRequest){

    return new Poll();
  }

}
