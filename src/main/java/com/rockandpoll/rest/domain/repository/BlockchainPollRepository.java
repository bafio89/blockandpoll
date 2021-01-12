package com.rockandpoll.rest.domain.repository;

import com.rockandpoll.rest.domain.model.BlockchainPoll;
import com.rockandpoll.rest.domain.model.Poll;

public interface BlockchainPollRepository {

  BlockchainPoll save(Poll poll);

}
