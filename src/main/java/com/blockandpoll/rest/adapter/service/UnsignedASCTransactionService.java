package com.blockandpoll.rest.adapter.service;

import static com.blockandpoll.rest.adapter.AlgorandUtils.headers;
import static com.blockandpoll.rest.adapter.AlgorandUtils.values;

import com.algorand.algosdk.account.Account;
import com.algorand.algosdk.crypto.TEALProgram;
import com.algorand.algosdk.transaction.Transaction;
import com.algorand.algosdk.v2.client.common.AlgodClient;
import com.blockandpoll.rest.adapter.PollTealParams;
import com.blockandpoll.rest.adapter.TealProgramFactory;
import com.blockandpoll.rest.adapter.converter.PollBlockchainAdapter;
import com.blockandpoll.rest.adapter.exceptions.NodeStatusException;
import com.blockandpoll.rest.domain.model.Poll;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UnsignedASCTransactionService {

  private Logger logger = LoggerFactory.getLogger(UnsignedASCTransactionService.class);

  private AlgodClient algodClient;
  private PollBlockchainAdapter pollBlockchainAdapter;
  private TealProgramFactory tealProgramFactory;
  private BuildApplicationCreateTransactionService buildApplicationCreateTransactionService;

  public UnsignedASCTransactionService(AlgodClient algodClient, PollBlockchainAdapter pollBlockchainAdapter,
      TealProgramFactory tealProgramFactory, BuildApplicationCreateTransactionService buildApplicationCreateTransactionService) {
    this.algodClient = algodClient;
    this.pollBlockchainAdapter = pollBlockchainAdapter;
    this.tealProgramFactory = tealProgramFactory;
    this.buildApplicationCreateTransactionService = buildApplicationCreateTransactionService;
  }

  public Transaction createUnsignedTxFor(Poll poll, Account account) {

    Long lastRound = getBlockChainLastRound();

    PollTealParams pollTealParams = pollBlockchainAdapter
        .fromPollToPollTealParams(poll, lastRound);

    TEALProgram approvalProgram = tealProgramFactory.createApprovalProgramFrom(pollTealParams);
    TEALProgram clearStateProgram = tealProgramFactory.createClearStateProgram();

    return buildApplicationCreateTransactionService
        .buildTransaction(pollTealParams, approvalProgram, clearStateProgram, account.getAddress().toString());
  }

  private Long getBlockChainLastRound() {

    Long lastRound;
    try {
      lastRound = algodClient.GetStatus().execute(headers, values).body().lastRound;
    } catch (Exception e) {
      logger.error("Something goes wrong getting last blockchain round", e);
      throw new NodeStatusException(e.getMessage());
    }
    return lastRound;
  }
}