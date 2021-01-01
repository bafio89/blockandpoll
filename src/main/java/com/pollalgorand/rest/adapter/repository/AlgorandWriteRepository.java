package com.pollalgorand.rest.adapter.repository;

import com.algorand.algosdk.transaction.Transaction;
import com.pollalgorand.rest.adapter.service.BuildOptinTransactionService;
import com.pollalgorand.rest.adapter.service.BuildVoteTransactionService;
import com.pollalgorand.rest.adapter.service.TransactionWriterService;
import com.pollalgorand.rest.domain.repository.BlockchainWriteRepository;
import com.pollalgorand.rest.domain.request.OptinAppRequest;
import com.pollalgorand.rest.domain.request.VoteAppRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AlgorandWriteRepository implements BlockchainWriteRepository {

  private Logger logger = LoggerFactory.getLogger(AlgorandASCPollRepository.class);

  private final BuildOptinTransactionService buildOptinTransactionService;
  private final BuildVoteTransactionService buildVoteTransactionService;
  private final TransactionWriterService transactionWriterService;

  public AlgorandWriteRepository(BuildOptinTransactionService buildOptinTransactionService,
      BuildVoteTransactionService buildVoteTransactionService,
      TransactionWriterService transactionWriterService) {

    this.buildOptinTransactionService = buildOptinTransactionService;
    this.buildVoteTransactionService = buildVoteTransactionService;
    this.transactionWriterService = transactionWriterService;
  }

  @Override
  public void optin(OptinAppRequest optinAppRequest) {

    try {
      Transaction unsignedTransaction = buildOptinTransactionService
          .buildTransaction(optinAppRequest);

      transactionWriterService.write(optinAppRequest.getAccount(), unsignedTransaction);

    } catch (Exception e) {
      logger.error("Something goes wrong sending transaction subscribing for app id {}",
          optinAppRequest.getAppId(), e);
      throw e;
    }
  }

  @Override
  public void vote(VoteAppRequest voteAppRequest) {

    try{
      Transaction unsignedTransaction = buildVoteTransactionService
          .buildTransaction(voteAppRequest);

      transactionWriterService.write(voteAppRequest.getAccount(), unsignedTransaction);
    } catch (Exception e) {
      logger.error("Something goes wrong sending transaction subscribing for app id {}",
          voteAppRequest.getAppId(), e);
      throw e;
    }
  }
}
