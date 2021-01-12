package com.pollalgorand.rest.domain;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.pollalgorand.rest.adapter.Clock;
import java.time.LocalDateTime;
import org.jmock.Expectations;
import org.jmock.auto.Mock;
import org.jmock.integration.junit4.JUnitRuleMockery;
import org.jmock.lib.legacy.ClassImposteriser;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

public class DateValidatorTest {

  public static final LocalDateTime START_DATE = LocalDateTime.of(2020, 11, 30, 0, 0);
  public static final LocalDateTime END_DATE = LocalDateTime.of(2020, 12, 31, 0, 0);
  @Rule
  public JUnitRuleMockery context = new JUnitRuleMockery(){{
    setImposteriser(ClassImposteriser.INSTANCE);
  }};

  @Mock
  private Clock clock;

  private DateValidator dateValidator;

  @Before
  public void setUp() {
    dateValidator = new DateValidator(clock);
  }

  @Test
  public void whenNowIsBetweenDates() {

    LocalDateTime now = LocalDateTime.of(2020, 12, 27, 0,0);

    context.checking(new Expectations(){{
      oneOf(clock).now();
      will(returnValue(now));
    }});

    assertTrue(dateValidator.isNowInInterval(START_DATE, END_DATE));
  }

  @Test
  public void whenNowIsAfterEnd() {

    LocalDateTime now = LocalDateTime.of(2021, 12, 27, 0,0);

    context.checking(new Expectations(){{
      oneOf(clock).now();
      will(returnValue(now));
    }});

    assertFalse(dateValidator.isNowInInterval(START_DATE, END_DATE));
  }

  @Test
  public void whenNowIsBeforeStart() {

    LocalDateTime now = LocalDateTime.of(2019, 12, 27, 0,0);

    context.checking(new Expectations(){{
      oneOf(clock).now();
      will(returnValue(now));
    }});

    assertFalse(dateValidator.isNowInInterval(START_DATE, END_DATE));
  }
}