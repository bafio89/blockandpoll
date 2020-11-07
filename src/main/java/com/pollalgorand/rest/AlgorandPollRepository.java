package com.pollalgorand.rest;

import com.algorand.algosdk.transaction.Transaction;
import com.algorand.algosdk.v2.client.common.AlgodClient;
import java.util.Optional;

public class AlgorandPollRepository implements PollRepository {

  private AlgodClient algodClient;
  private TealProgramFactory tealProgramFactory;
  private PollBlockchainParamsAdapter pollBlockchainParamsAdapter;

  public AlgorandPollRepository(AlgodClient algodClient,
      TealProgramFactory tealProgramFactory,
      PollBlockchainParamsAdapter pollBlockchainParamsAdapter) {

    this.algodClient = algodClient;
    this.tealProgramFactory = tealProgramFactory;
    this.pollBlockchainParamsAdapter = pollBlockchainParamsAdapter;
  }

  @Override
  public Optional<Poll> save(Poll poll) {

//    tealProgramFactory.createFrom(poll);
//
//    return Optional.of(new BlockchainPoll());

    return null;
  }

  @Override
  public Transaction createUnsignedTx(Poll poll) {

    tealProgramFactory.createApprovalProgramFrom(pollBlockchainParamsAdapter.fromPollToTransactionPoll(poll));

    tealProgramFactory.createClearStateProgram();

    return new Transaction();
  }
}
