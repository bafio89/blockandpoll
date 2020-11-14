package com.pollalgorand.rest;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import java.time.LocalDateTime;
import org.junit.Test;

public class AlgorandDateAdapterTest {

  @Test
  public void happyPath() {
    LocalDateTime now = LocalDateTime.now();
    LocalDateTime tomorrow = now.plusDays(1L);

    AlgorandDateAdapter algorandDateAdapter = new AlgorandDateAdapter(now);

    Long blockNumber = algorandDateAdapter.fromDateToBlockNumber(tomorrow, 1L);

    assertThat(blockNumber, is(19201L));
  }
}