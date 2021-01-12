package com.blockandpoll.rest.domain.repository;

import com.blockandpoll.rest.domain.model.BlockchainPoll;
import java.util.List;
import java.util.Optional;

public interface PollRepository {

   void save(BlockchainPoll poll);

   List<BlockchainPoll> find();

   Optional<BlockchainPoll> findBy(long appId);
}
