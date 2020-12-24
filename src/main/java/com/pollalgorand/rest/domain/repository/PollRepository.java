package com.pollalgorand.rest.domain.repository;

import com.pollalgorand.rest.domain.model.BlockchainPoll;
import java.util.List;

public interface PollRepository {

   void save(BlockchainPoll poll);

   List<BlockchainPoll> find();

   BlockchainPoll findBy(long appId);
}
