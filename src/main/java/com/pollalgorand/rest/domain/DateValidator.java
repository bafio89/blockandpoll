package com.pollalgorand.rest.domain;

import com.pollalgorand.rest.adapter.Clock;
import java.time.LocalDateTime;

public class DateValidator {

  private Clock clock;

  public DateValidator(Clock clock) {

    this.clock = clock;
  }

  public boolean isNowInInterval(LocalDateTime start,
      LocalDateTime end) {
    LocalDateTime now = clock.now();
    return now.isAfter(start) && now.isBefore(end);
  }
}
