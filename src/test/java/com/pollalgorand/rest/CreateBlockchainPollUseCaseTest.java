package com.pollalgorand.rest;

import static java.util.Arrays.asList;

import com.pollalgorand.rest.domain.model.Poll;
import com.pollalgorand.rest.domain.repository.PollRepository;
import com.pollalgorand.rest.domain.usecase.CreatePollUseCase;
import java.time.LocalDateTime;
import org.jmock.Expectations;
import org.jmock.auto.Mock;
import org.jmock.integration.junit4.JUnitRuleMockery;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class CreateBlockchainPollUseCaseTest {

  public static final LocalDateTime DATE = LocalDateTime.now();
  @Rule
  public JUnitRuleMockery context = new JUnitRuleMockery();

  @Rule public ExpectedException expectedException = ExpectedException.none();

  @Mock
  private PollRepository blockChainPollRepository;

  @Mock
  private PollRepository postgresRepository;

  private CreatePollUseCase createPollUseCase;

  @Before
  public void setUp() {
    createPollUseCase = new CreatePollUseCase(blockChainPollRepository, postgresRepository);
  }

//  @Test
//  public void happyPath() {
//
//    Poll createPollRequest = new Poll("A POLL NAME", DATE, DATE, DATE,
//        DATE, asList("Option 1", "Option 2"), "sender");
//
//    BlockchainPoll blockchainPollGeneratedFromBlockchain = new BlockchainPoll("A POLL TX ID");
//
//    context.checking(new Expectations() {{
//
//      oneOf(blockChainPollRepository).save(createPollRequest);
//      will(returnValue(Optional.of(blockchainPollGeneratedFromBlockchain)));
//
//      oneOf(postgresRepository).save(blockchainPollGeneratedFromBlockchain);
//    }});
//
//    Optional<Poll> poll = createPollUseCase.create(createPollRequest);
//
//    BlockchainPoll expectedBlockchainPoll = new BlockchainPoll("A POLL TX ID");
//
//    assertThat(poll, is(Optional.of(expectedBlockchainPoll)));
//
//  }

//  @Test
//  public void whenBlockchainPollCreationFails() {
//
//    Poll createPollRequest = new Poll("A POLL NAME", DATE, DATE, DATE,
//        DATE, asList("Option 1", "Option 2"), "sender");
//
//    context.checking(new Expectations() {{
//
//      oneOf(blockChainPollRepository).save(createPollRequest);
//      will(returnValue(Optional.empty()));
//
//      never(postgresRepository);
//    }});
//
//    Optional<Poll> poll = createPollUseCase.create(createPollRequest);
//
//    assertThat(poll, is(Optional.empty()));
//  }

//  @Test
//  public void whenPostgresRepositorySavingFails() {
//
//    Poll poll = new Poll("A POLL NAME", DATE, DATE, DATE,
//        DATE, asList("Option 1", "Option 2"), "sender");
//
//    BlockchainPoll blockchainPollGeneratedFromBlockchain = new BlockchainPoll("A POLL TX ID");
//
//    context.checking(new Expectations() {{
//
//      oneOf(blockChainPollRepository).save(poll);
//      will(returnValue(Optional.of(blockchainPollGeneratedFromBlockchain)));
//
//      oneOf(postgresRepository).save(blockchainPollGeneratedFromBlockchain);
//      will(throwException(new SavingPollException()));
//    }});
//
//    expectedException.expect(SavingPollException.class);
//
//    createPollUseCase.create(poll);
//
//  }


  @Test
  public void createTransactionHappyPath() {
    Poll poll = new Poll("A POLL NAME", DATE, DATE, DATE,
        DATE, asList("Option 1", "Option 2"), "sender", "mnemonicKey");

    context.checking(new Expectations(){{
      oneOf(blockChainPollRepository).createUnsignedTxFor(poll);
    }});

    createPollUseCase.createUnsignedTx(poll);

  }
}