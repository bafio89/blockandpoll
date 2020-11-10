package com.pollalgorand.rest;

import static java.nio.charset.StandardCharsets.UTF_8;
import static java.util.Arrays.asList;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;


import java.util.Date;
import java.util.List;
import org.junit.Test;

public class PollBlockchainParamsAdapterTest {

  public static final String A_POLL = "A_POLL";
  private PollBlockchainParamsAdapter pollBlockchainParamsAdapter;
  public static final String SENDER = "A_SENDER";

  @Test
  public void adaptToPollTealParams() {

    Date startVotingTime = new Date();
    Date endVotingTime = new Date();
    Date startSubscriptionTime = new Date();
    Date endSubscriptionTime = new Date();
    List<String> options = asList("Option1", "Option2");

    List<byte[]> optionsInByte = asList("Option1".getBytes(UTF_8), "Option2".getBytes(UTF_8));

    PollTealParams expectedTealParams = new PollTealParams(A_POLL.getBytes(UTF_8), startVotingTime,
        endVotingTime, startSubscriptionTime, endSubscriptionTime, optionsInByte,
        SENDER.getBytes(UTF_8));

    pollBlockchainParamsAdapter = new PollBlockchainParamsAdapter();

    Poll poll = new Poll(
        A_POLL, startVotingTime, endVotingTime, startSubscriptionTime,
        endSubscriptionTime, options, SENDER);

    PollTealParams pollTealParams = pollBlockchainParamsAdapter.fromPollToPollTealParams(poll);

    assertThat(pollTealParams, is(expectedTealParams));
  }
}