package com.rockandpoll.rest;

import static com.rockandpoll.rest.ByteConverteUtil.convertLongToByteArray;
import static java.nio.charset.StandardCharsets.UTF_8;
import static java.util.Arrays.asList;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import com.rockandpoll.rest.adapter.PollTealParams;
import com.rockandpoll.rest.adapter.converter.AlgorandDateAdapter;
import com.rockandpoll.rest.adapter.converter.PollBlockchainAdapter;
import com.rockandpoll.rest.domain.model.BlockchainPoll;
import com.rockandpoll.rest.domain.model.Poll;
import java.time.LocalDateTime;
import java.util.List;
import org.jmock.Expectations;
import org.jmock.auto.Mock;
import org.jmock.integration.junit4.JUnitRuleMockery;
import org.jmock.lib.legacy.ClassImposteriser;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

public class PollBlockchainAdapterTest {

  public static final Long A_START_SUBS_BLOCK_NUMBER = 1L;
  public static final Long A_END_SUBS_BLOCK_NUMBER = 2L;
  public static final Long A_START_VOTE_BLOCK_NUMBER = 3L;
  public static final Long A_END_VOTE_BLOCK_NUMBER = 4L;
  public static final String MNEMONIC_KEY = "mnemonicKey";
  public static final String DESCRIPTION = "description";
  public static final long APP_ID = 123L;
  public static final String QUESTION = "QUESTION";

  @Rule
  public JUnitRuleMockery context = new JUnitRuleMockery() {{
    setImposteriser(ClassImposteriser.INSTANCE);
  }};

  public static final String A_POLL = "A_POLL";

  @Mock
  private AlgorandDateAdapter algorandDateAdapter;

  private PollBlockchainAdapter pollBlockchainAdapter;

  public static final String SENDER = "A_SENDER";
  public static final Long LAST_ROUND = 1L;
  private LocalDateTime now = LocalDateTime.now();

  @Before
  public void setUp() {

    pollBlockchainAdapter = new PollBlockchainAdapter(algorandDateAdapter);
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
        options, SENDER, MNEMONIC_KEY, DESCRIPTION);

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

    PollTealParams pollTealParams = pollBlockchainAdapter.fromPollToPollTealParams(poll,
        LAST_ROUND);

    assertThat(pollTealParams.getOptions().get(0),
        is(expectedTealParams.getOptions().get(0)));

    assertThat(pollTealParams.getOptions().get(1),
        is(expectedTealParams.getOptions().get(1)));

    assertThat(pollTealParams, is(expectedTealParams));
  }

  @Test
  public void adaptFromPollToBlockchainPoll() {

    BlockchainPoll expectedBlockchainPoll = expectedBlockchainPoll();

    assertThat(expectedBlockchainPoll, is(pollBlockchainAdapter.fromPollToBlockchainPoll(aPoll(), APP_ID)));
  }

  private Poll aPoll() {
    return new Poll("POLL_NAME", now, now, now, now,
        asList("OPTION_1", "OPTION_2"), QUESTION, "MEMONIC_KEY", "DESCRIPTION");
  }

  private BlockchainPoll expectedBlockchainPoll() {
    return new BlockchainPoll(APP_ID, "POLL_NAME", QUESTION, now, now, now, now,
        asList("OPTION_1", "OPTION_2"),  "MEMONIC_KEY", "DESCRIPTION");
  }
}