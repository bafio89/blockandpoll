package com.pollalgorand.rest;

import java.util.Optional;

public interface PollRepository {

  Optional<Poll> save(Poll blockchainPoll);
}
