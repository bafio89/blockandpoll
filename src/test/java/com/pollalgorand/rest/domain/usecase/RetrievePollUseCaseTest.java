package com.pollalgorand.rest.domain.usecase;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.rules.ExpectedException.none;

import com.pollalgorand.rest.domain.ApplicationInfoFromBlockchain;
import com.pollalgorand.rest.domain.exceptions.PollNotFoundException;
import com.pollalgorand.rest.domain.model.BlockchainPoll;
import com.pollalgorand.rest.domain.model.EnrichedBlockchainPoll;
import com.pollalgorand.rest.domain.repository.BlockchainReadRepository;
import com.pollalgorand.rest.domain.repository.PollRepository;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.Optional;
import org.jmock.Expectations;
import org.jmock.auto.Mock;
import org.jmock.integration.junit4.JUnitRuleMockery;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class RetrievePollUseCaseTest {

  public static final long APP_ID = 123L;
  public static final String AN_OPTION = "AN_OPTION";
  @Rule
  public JUnitRuleMockery context = new JUnitRuleMockery();

  @Rule public final ExpectedException expectedException = none();

  @Mock
  private PollRepository pollRepository;

  @Mock
  private BlockchainReadRepository blockchainReadRepository;

  private RetrievePollUseCase retrievePollUseCase;

  @Before
  public void setUp() {
    retrievePollUseCase = new RetrievePollUseCase(pollRepository, blockchainReadRepository);
  }

  @Test
  public void findPollByAppId() {

    BlockchainPoll blockchainPoll = new BlockchainPoll();
    HashMap<String, BigInteger> optionsVotes = new HashMap<>();
    optionsVotes.put(AN_OPTION, BigInteger.valueOf(2));
    ApplicationInfoFromBlockchain applicationInfoFromBlockchain = new ApplicationInfoFromBlockchain(optionsVotes, 2);

    EnrichedBlockchainPoll expectedEnrichedBlockchainPoll = new EnrichedBlockchainPoll(blockchainPoll, applicationInfoFromBlockchain);

    context.checking(new Expectations(){{
      oneOf(pollRepository).findBy(APP_ID);
      will(returnValue(Optional.of(blockchainPoll)));

      oneOf(blockchainReadRepository).findApplicationInfoBy(blockchainPoll);
      will(returnValue(applicationInfoFromBlockchain));
    }});

    assertThat(retrievePollUseCase.findPollByAppId(APP_ID), is(expectedEnrichedBlockchainPoll));
  }

  @Test
  public void findPollByAppIdWhenPollIsNotFound() {

    context.checking(new Expectations(){{
      oneOf(pollRepository).findBy(APP_ID);
      will(returnValue(Optional.empty()));

      never(blockchainReadRepository);
    }});

    expectedException.expect(PollNotFoundException.class);
    expectedException.expectMessage("Impossible to found the poll with appId: 123");

    retrievePollUseCase.findPollByAppId(APP_ID);
  }
}