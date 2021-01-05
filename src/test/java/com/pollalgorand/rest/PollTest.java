package com.pollalgorand.rest;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import com.pollalgorand.rest.domain.exceptions.IllegalPollParameterException;
import com.pollalgorand.rest.domain.model.Poll;
import java.time.LocalDateTime;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class PollTest {

  public static final String INVALID_POLL_NAME = "VERY VERY VERY VERY VERY VERY VERY VERY VERY VERY VERY LONG POLL NAME";
  public static final String A_NAME = "A NAME";
  public static final String INVALID_OPTION_NAME = "A VERY VERY VERY VERY VERY VERY LONG OPTION NAME";
  @Rule
  public ExpectedException expectedException = ExpectedException.none();

  @Test
  public void whenStartSubscriptionDateIsAfterEndSubscriptionDate() {

    expectedException.expect(IllegalPollParameterException.class);
    expectedException.expectMessage("Invalid poll parameters: start subscription date is after end subscription date");

    aPollWith(LocalDateTime.of(2020, 11, 29, 0, 0),
        LocalDateTime.of(2020, 11, 11, 0, 0),
        null, null);

  }

  private Poll aPollWith(LocalDateTime startSubscriptionTime, LocalDateTime endSubscriptionTime,
      LocalDateTime startVotingTime, LocalDateTime endVotingTime) {
    return new Poll(A_NAME, startSubscriptionTime,
        endSubscriptionTime,
        startVotingTime, endVotingTime, emptyList(), "", "mnemonicKey", "description");
  }

  @Test
  public void whenStartVotingDateIsAfterEndVotingDate() {

    expectedException.expect(IllegalPollParameterException.class);
    expectedException.expectMessage("Invalid poll parameters: start voting date is after end voting date");

    aPollWith(LocalDateTime.of(2020,11, 29, 0,0),
        LocalDateTime.of(2020,11, 30, 0,0),
        LocalDateTime.of(2020,11, 29, 0,0),
        LocalDateTime.of(2020,11, 11, 0,0));

  }

  @Test
  public void whenPollNameIsTooLong() {

    expectedException.expect(IllegalPollParameterException.class);
    expectedException.expectMessage("Invalid poll parameters: poll name is too long");

    new Poll(INVALID_POLL_NAME, null,null,null,null,null,null, "mnemonicKey", "description");
  }

  @Test
  public void whenAnOptionNameIsTooLong() {
    expectedException.expect(IllegalPollParameterException.class);
    expectedException.expectMessage("Invalid poll parameters: at least an option name is too long");

    new Poll(A_NAME, null,null,null,null, asList(INVALID_OPTION_NAME),null, "mnemonicKey",
        "description");

  }

  @Test
  public void whenPollIsOpen() {
    LocalDateTime now = LocalDateTime.now();
    LocalDateTime startVotingTime = now.minusDays(1);
    LocalDateTime endVotingTime = now.plusDays(1);
    LocalDateTime startSubscriptionTime = now.minusDays(3);
    LocalDateTime endSubscriptionTime = now.minusDays(2);

    Poll poll = aPollWith(startSubscriptionTime, endSubscriptionTime, startVotingTime, endVotingTime);

    assertThat(poll.pollStatus(), is("Open"));
  }

  @Test
  public void whenSubscriptionIsOpen() {
    LocalDateTime now = LocalDateTime.now();
    LocalDateTime startVotingTime = now.plusDays(1);
    LocalDateTime endVotingTime = now.plusDays(2);
    LocalDateTime startSubscriptionTime = now.minusDays(3);
    LocalDateTime endSubscriptionTime = now.plusDays(2);

    Poll poll = aPollWith(startSubscriptionTime, endSubscriptionTime, startVotingTime, endVotingTime);

    assertThat(poll.pollStatus(), is("Subscription open"));
  }


  @Test
  public void whenPollIsExpired() {
    LocalDateTime now = LocalDateTime.now();
    LocalDateTime startVotingTime = now.minusDays(3);
    LocalDateTime endVotingTime = now.minusDays(1);
    LocalDateTime startSubscriptionTime = now.minusDays(4);
    LocalDateTime endSubscriptionTime = now.minusDays(3);

    Poll poll = aPollWith(startSubscriptionTime, endSubscriptionTime, startVotingTime, endVotingTime);

    assertThat(poll.pollStatus(), is("Expired"));
  }

  @Test
  public void whenPollIsNotYetOpen() {
    LocalDateTime now = LocalDateTime.now();
    LocalDateTime startVotingTime = now.plusDays(3);
    LocalDateTime endVotingTime = now.plusDays(5);
    LocalDateTime startSubscriptionTime = now.plusDays(2);
    LocalDateTime endSubscriptionTime = now.plusDays(3);

    Poll poll = aPollWith(startSubscriptionTime, endSubscriptionTime, startVotingTime, endVotingTime);

    assertThat(poll.pollStatus(), is("Not yet open"));
  }
}