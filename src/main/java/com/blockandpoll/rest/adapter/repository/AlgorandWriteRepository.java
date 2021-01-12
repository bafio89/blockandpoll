package com.blockandpoll.rest.adapter.repository;

import com.algorand.algosdk.transaction.Transaction;
import com.blockandpoll.rest.adapter.exceptions.OptinException;
import com.blockandpoll.rest.adapter.exceptions.VoteException;
import com.blockandpoll.rest.adapter.service.BuildOptinTransactionService;
import com.blockandpoll.rest.adapter.service.BuildVoteTransactionService;
import com.blockandpoll.rest.adapter.service.TransactionWriterService;
import com.blockandpoll.rest.domain.repository.BlockchainWriteRepository;
import com.blockandpoll.rest.domain.request.OptinAppRequest;
import com.blockandpoll.rest.domain.request.VoteAppRequest;
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
      logger.error("Something goes wrong sending transaction subscribing for app id {} from address {}",
          optinAppRequest.getAppId(), optinAppRequest.getAccount().getAddress().toString(), e);
      throw new OptinException(optinAppRequest.getAppId(), optinAppRequest.getAccount().getAddress().toString());
    }
  }

  @Override
  public void vote(VoteAppRequest voteAppRequest) {

    try{
      Transaction unsignedTransaction = buildVoteTransactionService
          .buildTransaction(voteAppRequest);

      transactionWriterService.write(voteAppRequest.getAccount(), unsignedTransaction);
    } catch (Exception e) {
      logger.error("Something goes wrong sending vote transaction for app id {} from address {}",
          voteAppRequest.getAppId(), voteAppRequest.getAccount().getAddress().toString(), e);
      throw new VoteException(voteAppRequest.getAppId(), voteAppRequest.getAccount().getAddress().toString());
    }
  }
}
