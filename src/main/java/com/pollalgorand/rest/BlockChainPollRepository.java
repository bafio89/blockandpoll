package com.pollalgorand.rest;

import java.util.Optional;

public interface BlockChainPollRepository {

  Optional<BlockchainPoll> save(Poll poll);
}
