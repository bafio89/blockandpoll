package com.pollalgorand.rest;

import java.util.Optional;

public interface BlockChainPollRepository {

  Optional<Poll> save(CreatePollRequest createPollRequest);
}
