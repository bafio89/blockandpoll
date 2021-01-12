package com.blockandpoll.rest.domain.repository;

import com.blockandpoll.rest.domain.model.BlockchainPoll;
import com.blockandpoll.rest.domain.model.Poll;

public interface BlockchainPollRepository {

  BlockchainPoll save(Poll poll);

}
