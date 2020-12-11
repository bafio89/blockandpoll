package com.pollalgorand.rest.domain.repository;

import com.algorand.algosdk.transaction.Transaction;
import com.pollalgorand.rest.domain.model.Poll;
import java.util.Optional;

public interface PollRepository {

  Optional<Poll> save(Poll poll);

  Transaction createUnsignedTxFor(Poll poll);
}