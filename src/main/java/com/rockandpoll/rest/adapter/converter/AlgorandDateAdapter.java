package com.rockandpoll.rest.adapter.converter;

import com.rockandpoll.rest.adapter.Clock;
import com.rockandpoll.rest.adapter.exceptions.PastDateException;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public class AlgorandDateAdapter {

  public static final double MEAN_TIME_FOR_BLOCK_GENERATION_IN_SECOND = 4.5;
  private final Clock clock;

  public AlgorandDateAdapter(Clock clock) {
    this.clock = clock;
  }

  public Long fromDateToBlockNumber(LocalDateTime date, Long lastRound) {

    long duration = ChronoUnit.SECONDS.between(clock.now().withHour(0).withMinute(0).withSecond(0), date);

    if(duration <= 0){
      throw new PastDateException(String.format("Something gone wrong with date selection. Date %s is previous than now", date));
    }

    long round = Math.round(duration / MEAN_TIME_FOR_BLOCK_GENERATION_IN_SECOND);

    return lastRound + round;
  }
}
