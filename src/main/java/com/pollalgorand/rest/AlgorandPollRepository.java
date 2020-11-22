package com.pollalgorand.rest;

import static java.util.stream.Collectors.joining;

import com.algorand.algosdk.crypto.TEALProgram;
import com.algorand.algosdk.logic.StateSchema;
import com.algorand.algosdk.transaction.Transaction;
import com.algorand.algosdk.v2.client.common.AlgodClient;
import com.algorand.algosdk.v2.client.model.CompileResponse;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
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
  public Transaction createUnsignedTx(Poll poll) throws Exception {

    Transaction transaction = null;

    Long lastRound = algodClient.GetStatus().execute().body().lastRound;

    PollTealParams pollTealParams = pollBlockchainParamsAdapter.fromPollToPollTealParams(poll, lastRound);
    TEALProgram approvalProgramFrom = tealProgramFactory.createApprovalProgramFrom(pollTealParams);

    TEALProgram clearStateProgram = tealProgramFactory.createClearStateProgram();

    try {
     transaction = Transaction.ApplicationCreateTransactionBuilder()
          .sender(poll.getSender())
          .suggestedParams(algodClient.TransactionParams().execute().body())
          .approvalProgram(approvalProgramFrom)
          .clearStateProgram(clearStateProgram)
          .globalStateSchema(new StateSchema(6, 1))
          .localStateSchema(new StateSchema(0, 1))
          .build();
    }catch (IllegalArgumentException e) {
      e.printStackTrace();
      throw new InvalidSenderAddressException(e);
    }catch (Exception e) {
      e.printStackTrace();
      throw new RuntimeException(e);
    }

    return transaction;
  }
}
