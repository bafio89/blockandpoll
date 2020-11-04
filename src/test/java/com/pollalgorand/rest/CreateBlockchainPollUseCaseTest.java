package com.pollalgorand.rest;

import java.util.Optional;
import org.jmock.Expectations;
import org.jmock.auto.Mock;

import static java.util.Arrays.asList;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import java.util.Date;
import org.jmock.integration.junit4.JUnitRuleMockery;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class CreateBlockchainPollUseCaseTest {

  @Rule
  public JUnitRuleMockery context = new JUnitRuleMockery();

  @Rule public ExpectedException expectedException = ExpectedException.none();

  @Mock
  private BlockChainPollRepository blockChainPollRepository;

  @Mock
  private PollRepository postgresRepository;

  private CreatePollUseCase createPollUseCase;

  @Before
  public void setUp() {
    createPollUseCase = new CreatePollUseCase(blockChainPollRepository, postgresRepository);
  }

  @Test
  public void happyPath() {

    Poll createPollRequest = new Poll("A POLL NAME", new Date(),
        new Date(), new Date(), new Date(), asList("Option 1", "Option 2"));

    BlockchainPoll blockchainPollGeneratedFromBlockchain = new BlockchainPoll("A POLL NAME");

    context.checking(new Expectations() {{

      oneOf(blockChainPollRepository).save(createPollRequest);
      will(returnValue(Optional.of(blockchainPollGeneratedFromBlockchain)));

      oneOf(postgresRepository).save(blockchainPollGeneratedFromBlockchain);
    }});

    Optional<BlockchainPoll> poll = createPollUseCase.create(createPollRequest);

    BlockchainPoll expectedBlockchainPoll = new BlockchainPoll("A POLL NAME");

    assertThat(poll, is(Optional.of(expectedBlockchainPoll)));

  }

  @Test
  public void whenBlockchainPollCreationFails() {

    Poll createPollRequest = new Poll("A POLL NAME", new Date(),
        new Date(), new Date(), new Date(), asList("Option 1", "Option 2"));

    context.checking(new Expectations() {{

      oneOf(blockChainPollRepository).save(createPollRequest);
      will(returnValue(Optional.empty()));

      never(postgresRepository);
    }});

    Optional<BlockchainPoll> poll = createPollUseCase.create(createPollRequest);

    assertThat(poll, is(Optional.empty()));
  }

  @Test
  public void whenPostgresRepositorySavingFails() {

    Poll poll = new Poll("A POLL NAME", new Date(),
        new Date(), new Date(), new Date(), asList("Option 1", "Option 2"));

    BlockchainPoll blockchainPollGeneratedFromBlockchain = new BlockchainPoll("A POLL NAME");

    context.checking(new Expectations() {{

      oneOf(blockChainPollRepository).save(poll);
      will(returnValue(Optional.of(blockchainPollGeneratedFromBlockchain)));

      oneOf(postgresRepository).save(blockchainPollGeneratedFromBlockchain);
      will(throwException(new SavingPollException()));
    }});

    expectedException.expect(SavingPollException.class);

    createPollUseCase.create(poll);

  }
}