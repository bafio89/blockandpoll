package com.pollalgorand.rest;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public class AlgorandDateAdapter {

  private final LocalDateTime now;

  public AlgorandDateAdapter(LocalDateTime now) {
    this.now = now;
  }

  public Long fromDateToBlockNumber(LocalDateTime date, Long lastRound) {

    long duration = ChronoUnit.SECONDS.between(now, date);

    long round = Math.round(duration / 4.5);

    return lastRound + round;
  }
}
