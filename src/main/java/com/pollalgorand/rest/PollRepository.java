package com.pollalgorand.rest;

import com.algorand.algosdk.transaction.Transaction;
import java.util.Optional;

public interface PollRepository {

  Optional<Poll> save(Poll poll);

  Transaction createUnsignedTx(Poll poll);
}
