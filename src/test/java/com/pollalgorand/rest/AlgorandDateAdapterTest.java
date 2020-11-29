package com.pollalgorand.rest;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.rules.ExpectedException.none;

import java.time.LocalDateTime;
import org.jmock.Expectations;
import org.jmock.auto.Mock;
import org.jmock.integration.junit4.JUnitRuleMockery;
import org.jmock.lib.legacy.ClassImposteriser;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class AlgorandDateAdapterTest {

  @Rule
  public JUnitRuleMockery context = new JUnitRuleMockery(){{
    setImposteriser(ClassImposteriser.INSTANCE);
  }};

  private AlgorandDateAdapter algorandDateAdapter;

  private LocalDateTime now = LocalDateTime.now();

  @Mock
  private Clock clock;

  @Rule public final ExpectedException expectedException = none();

  @Before
  public void setUp() {
     algorandDateAdapter = new AlgorandDateAdapter(clock);
  }

  @Test
  public void happyPath() {
    LocalDateTime tomorrow = now.plusDays(1L);

    context.checking(new Expectations(){{
      oneOf(clock).now();
      will(returnValue(now));
    }});

    Long blockNumber = algorandDateAdapter.fromDateToBlockNumber(tomorrow, 1L);

    assertThat(blockNumber, is(19201L));
  }

  @Test
  public void whenDateIsInThePast() {
    LocalDateTime yersterday = now.minusDays(1L);

    context.checking(new Expectations(){{
      oneOf(clock).now();
      will(returnValue(now));
    }});

    expectedException.expect(PastDateException.class);
    expectedException.expectMessage(String.format("Something gone wrong with date selection. Date %s is previous than now", yersterday));

    algorandDateAdapter.fromDateToBlockNumber(yersterday, 1L);

  }
}