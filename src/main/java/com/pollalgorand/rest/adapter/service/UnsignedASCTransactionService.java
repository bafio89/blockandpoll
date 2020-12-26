package com.pollalgorand.rest.adapter.service;

import static com.pollalgorand.rest.adapter.AlgorandUtils.headers;
import static com.pollalgorand.rest.adapter.AlgorandUtils.values;

import com.algorand.algosdk.crypto.TEALProgram;
import com.algorand.algosdk.transaction.Transaction;
import com.algorand.algosdk.v2.client.common.AlgodClient;
import com.pollalgorand.rest.adapter.PollTealParams;
import com.pollalgorand.rest.adapter.TealProgramFactory;
import com.pollalgorand.rest.adapter.converter.PollBlockchainParamsAdapter;
import com.pollalgorand.rest.adapter.exceptions.NodeStatusException;
import com.pollalgorand.rest.domain.model.Poll;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UnsignedASCTransactionService {

  private Logger logger = LoggerFactory.getLogger(UnsignedASCTransactionService.class);

  private AlgodClient algodClient;
  private PollBlockchainParamsAdapter pollBlockchainParamsAdapter;
  private TealProgramFactory tealProgramFactory;
  private BuildTransactionService buildTransactionService;

  public UnsignedASCTransactionService(AlgodClient algodClient, PollBlockchainParamsAdapter pollBlockchainParamsAdapter,
      TealProgramFactory tealProgramFactory, BuildTransactionService buildTransactionService) {
    this.algodClient = algodClient;
    this.pollBlockchainParamsAdapter = pollBlockchainParamsAdapter;
    this.tealProgramFactory = tealProgramFactory;
    this.buildTransactionService = buildTransactionService;
  }

  public Transaction createUnsignedTxFor(Poll poll) {

    Long lastRound = getBlockChainLastRound();

    PollTealParams pollTealParams = pollBlockchainParamsAdapter
        .fromPollToPollTealParams(poll, lastRound);

    TEALProgram approvalProgram = tealProgramFactory.createApprovalProgramFrom(pollTealParams);
    TEALProgram clearStateProgram = tealProgramFactory.createClearStateProgram();

    return buildTransactionService.buildTransaction(pollTealParams, approvalProgram, clearStateProgram, poll.getSender());
  }

  private Long getBlockChainLastRound() {

    Long lastRound;
    try {
      lastRound = algodClient.GetStatus().execute(headers, values).body().lastRound;
    } catch (Exception e) {
      logger.error("Something goes wrong getting last blockchain round", e);
      throw new NodeStatusException(e);
    }
    return lastRound;
  }
}