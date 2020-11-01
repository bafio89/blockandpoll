package com.pollalgorand.rest;


import static java.util.Arrays.asList;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import java.util.Date;
import org.junit.jupiter.api.Test;

public class CreatePollUseCaseTest {

  private BlockChainRepository blockChainRepository;
  private PollRepository postgresRepository;

  @Test
  void happyPath() {

    CreatePollUseCase createPollUseCase = new CreatePollUseCase(blockChainRepository, postgresRepository);

    CreatePollRequest createPollRequest = new CreatePollRequest("A POLL NAME", new Date(),
        new Date(), new Date(), new Date(), asList("Option 1", "Option 2"));

    Poll poll = createPollUseCase.create(createPollRequest);

    Poll expectedPoll = new Poll();

    assertThat(poll, is(expectedPoll));

  }
}