package com.pollalgorand.rest;

import static java.util.Arrays.asList;

import com.algorand.algosdk.crypto.TEALProgram;
import com.algorand.algosdk.logic.StateSchema;
import com.algorand.algosdk.transaction.Transaction;
import com.algorand.algosdk.v2.client.common.AlgodClient;
import java.util.List;
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
  public Transaction createUnsignedTxFor(Poll poll) {

    Transaction transaction;

    Long lastRound = getBlockChainLastRound();

    PollTealParams pollTealParams = pollBlockchainParamsAdapter
        .fromPollToPollTealParams(poll, lastRound);

    TEALProgram approvalProgramFrom = tealProgramFactory.createApprovalProgramFrom(pollTealParams);
    TEALProgram clearStateProgram = tealProgramFactory.createClearStateProgram();

    try {
      transaction = Transaction.ApplicationCreateTransactionBuilder()
          .sender(poll.getSender())
          .args(arguments(pollTealParams))
          .suggestedParams(algodClient.TransactionParams().execute().body())
          .approvalProgram(approvalProgramFrom)
          .clearStateProgram(clearStateProgram)
          .globalStateSchema(new StateSchema(6, 1))
          .localStateSchema(new StateSchema(0, 1))
          .build();
    } catch (IllegalArgumentException e) {
      throw new InvalidSenderAddressException(e);
    } catch (Exception e) {
      throw new BlockChainParameterException(e);
    }

    return transaction;
  }

  private Long getBlockChainLastRound() {
    Long lastRound;
    try {
      lastRound = algodClient.GetStatus().execute().body().lastRound;
    } catch (Exception e) {
      throw new NodeStatusException(e);
    }
    return lastRound;
  }

  private List<byte[]> arguments(PollTealParams pollTealParams) {

    return asList(pollTealParams.getStartSubscriptionTime(),
        pollTealParams.getEndSubscriptionTime(),
        pollTealParams.getStartVotingTime(),
        pollTealParams.getEndVotingTime());
  }
}
