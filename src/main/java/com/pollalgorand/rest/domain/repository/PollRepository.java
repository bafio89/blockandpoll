package com.pollalgorand.rest.domain.repository;

import com.pollalgorand.rest.domain.model.BlockchainPoll;

public interface PollRepository {

   void save(BlockchainPoll poll);

}
