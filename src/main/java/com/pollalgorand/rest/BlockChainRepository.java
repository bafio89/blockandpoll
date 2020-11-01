package com.pollalgorand.rest;

import java.util.Optional;

public interface BlockChainRepository {

  Optional<Poll> save(CreatePollRequest createPollRequest);
}
