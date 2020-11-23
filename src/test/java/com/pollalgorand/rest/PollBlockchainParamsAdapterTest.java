package com.pollalgorand.rest;

import static java.nio.charset.StandardCharsets.UTF_8;
import static java.util.Arrays.asList;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import org.jmock.Expectations;
import org.jmock.auto.Mock;
import org.jmock.integration.junit4.JUnitRuleMockery;
import org.jmock.lib.legacy.ClassImposteriser;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

public class PollBlockchainParamsAdapterTest {

  public static final long A_BLOCK_NUMBER = 1L;
  @Rule
  public JUnitRuleMockery context = new JUnitRuleMockery(){{
    setImposteriser(ClassImposteriser.INSTANCE);
  }};

  public static final String A_POLL = "A_POLL";

  @Mock
  private AlgorandDateAdapter algorandDateAdapter;

  private PollBlockchainParamsAdapter pollBlockchainParamsAdapter;

  public static final String SENDER = "A_SENDER";
  public static final Long LAST_ROUND = 1L;

  @Before
  public void setUp() {

    pollBlockchainParamsAdapter = new PollBlockchainParamsAdapter(algorandDateAdapter);
  }

  @Test
  public void adaptToPollTealParams() {

    LocalDateTime startVotingTime = LocalDateTime.now();
    LocalDateTime endVotingTime = LocalDateTime.now();
    LocalDateTime startSubscriptionTime = LocalDateTime.now();
    LocalDateTime endSubscriptionTime = LocalDateTime.now();
    List<String> options = asList("Option1", "Option2");

    PollTealParams expectedTealParams = new PollTealParams(A_POLL.getBytes(UTF_8), A_BLOCK_NUMBER,
        A_BLOCK_NUMBER, A_BLOCK_NUMBER, A_BLOCK_NUMBER, options,
        SENDER.getBytes(UTF_8));

    Poll poll = new Poll(
        A_POLL, startVotingTime, endVotingTime, startSubscriptionTime,
        endSubscriptionTime, options, SENDER);

    context.checking(new Expectations(){{
      oneOf(algorandDateAdapter).fromDateToBlockNumber(startSubscriptionTime, LAST_ROUND);
      will(returnValue(A_BLOCK_NUMBER));
      oneOf(algorandDateAdapter).fromDateToBlockNumber(endSubscriptionTime, LAST_ROUND);
      will(returnValue(A_BLOCK_NUMBER));
      oneOf(algorandDateAdapter).fromDateToBlockNumber(startVotingTime, LAST_ROUND);
      will(returnValue(A_BLOCK_NUMBER));
      oneOf(algorandDateAdapter).fromDateToBlockNumber(endVotingTime, LAST_ROUND);
      will(returnValue(A_BLOCK_NUMBER));
    }});

    PollTealParams pollTealParams = pollBlockchainParamsAdapter.fromPollToPollTealParams(poll,
        LAST_ROUND);

    assertThat(pollTealParams.getOptions().get(0),
        is(expectedTealParams.getOptions().get(0)));

    assertThat(pollTealParams.getOptions().get(1),
        is(expectedTealParams.getOptions().get(1)));

    assertThat(pollTealParams, is(expectedTealParams));
  }

}