package com.pollalgorand.rest;

import static com.pollalgorand.rest.ByteConverteUtil.convertLongToByteArray;
import static java.nio.charset.StandardCharsets.UTF_8;
import static java.util.Arrays.asList;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import com.pollalgorand.rest.adapter.PollTealParams;
import com.pollalgorand.rest.adapter.converter.AlgorandDateAdapter;
import com.pollalgorand.rest.adapter.converter.PollBlockchainParamsAdapter;
import com.pollalgorand.rest.domain.model.Poll;
import java.time.LocalDateTime;
import java.util.List;
import org.jmock.Expectations;
import org.jmock.auto.Mock;
import org.jmock.integration.junit4.JUnitRuleMockery;
import org.jmock.lib.legacy.ClassImposteriser;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

public class PollBlockchainParamsAdapterTest {

  public static final Long A_START_SUBS_BLOCK_NUMBER = 1L;
  public static final Long A_END_SUBS_BLOCK_NUMBER = 2L;
  public static final Long A_START_VOTE_BLOCK_NUMBER = 3L;
  public static final Long A_END_VOTE_BLOCK_NUMBER = 4L;

  @Rule
  public JUnitRuleMockery context = new JUnitRuleMockery() {{
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

    LocalDateTime startSubscriptionTime = LocalDateTime.now().plusDays(1L);
    LocalDateTime endSubscriptionTime = LocalDateTime.now().plusDays(2L);
    LocalDateTime startVotingTime = LocalDateTime.now().plusDays(3L);
    LocalDateTime endVotingTime = LocalDateTime.now().plusDays(4L);
    List<String> options = asList("Option1", "Option2");

    PollTealParams expectedTealParams = new PollTealParams(A_POLL.getBytes(UTF_8),
        convertLongToByteArray(A_START_SUBS_BLOCK_NUMBER),
        convertLongToByteArray(A_END_SUBS_BLOCK_NUMBER),
        convertLongToByteArray(A_START_VOTE_BLOCK_NUMBER),
        convertLongToByteArray(A_END_VOTE_BLOCK_NUMBER), options,
        SENDER.getBytes(UTF_8));

    Poll poll = new Poll(
        A_POLL, startSubscriptionTime, endSubscriptionTime, startVotingTime, endVotingTime,
        options, SENDER, "mnemonicKey");

    context.checking(new Expectations() {{
      oneOf(algorandDateAdapter).fromDateToBlockNumber(startSubscriptionTime, LAST_ROUND);
      will(returnValue(A_START_SUBS_BLOCK_NUMBER));
      oneOf(algorandDateAdapter).fromDateToBlockNumber(endSubscriptionTime, LAST_ROUND);
      will(returnValue(A_END_SUBS_BLOCK_NUMBER));
      oneOf(algorandDateAdapter).fromDateToBlockNumber(startVotingTime, LAST_ROUND);
      will(returnValue(A_START_VOTE_BLOCK_NUMBER));
      oneOf(algorandDateAdapter).fromDateToBlockNumber(endVotingTime, LAST_ROUND);
      will(returnValue(A_END_VOTE_BLOCK_NUMBER));
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