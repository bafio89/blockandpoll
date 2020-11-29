package com.pollalgorand.rest;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;

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

    new Poll(A_NAME, LocalDateTime.of(2020,11, 29, 0,0),
        LocalDateTime.of(2020,11, 11, 0,0),
        null, null, emptyList(), "");

  }

  @Test
  public void whenStartVotingDateIsAfterEndVotingDate() {

    expectedException.expect(IllegalPollParameterException.class);
    expectedException.expectMessage("Invalid poll parameters: start voting date is after end voting date");

    new Poll(A_NAME, LocalDateTime.of(2020,11, 29, 0,0),
        LocalDateTime.of(2020,11, 30, 0,0),
        LocalDateTime.of(2020,11, 29, 0,0),
        LocalDateTime.of(2020,11, 11, 0,0),
        emptyList(),
        "");

  }

  @Test
  public void whenPollNameIsTooLong() {

    expectedException.expect(IllegalPollParameterException.class);
    expectedException.expectMessage("Invalid poll parameters: poll name is too long");

    new Poll(INVALID_POLL_NAME, null,null,null,null,null,null);
  }

  @Test
  public void whenAnOptionNameIsTooLong() {
    expectedException.expect(IllegalPollParameterException.class);
    expectedException.expectMessage("Invalid poll parameters: at least an option name is too long");

    new Poll(A_NAME, null,null,null,null, asList(INVALID_OPTION_NAME),null);

  }
}