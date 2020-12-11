package com.pollalgorand.rest.domain.repository;

import com.pollalgorand.rest.domain.model.BlockchainPoll;
import com.pollalgorand.rest.domain.model.Poll;
import java.util.Optional;

public interface BlockChainPollRepository {

  Optional<BlockchainPoll> save(Poll poll);
}
