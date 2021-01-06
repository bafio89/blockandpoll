package com.pollalgorand.rest.domain.repository;

import com.pollalgorand.rest.domain.model.BlockchainPoll;
import com.pollalgorand.rest.domain.model.Poll;

public interface BlockchainPollRepository {

  BlockchainPoll save(Poll poll);

}
