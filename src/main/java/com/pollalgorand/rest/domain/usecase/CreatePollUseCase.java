package com.pollalgorand.rest.domain.usecase;


import com.algorand.algosdk.account.Account;
import com.algorand.algosdk.transaction.Transaction;
import com.pollalgorand.rest.adapter.service.AccountCreatorService;
import com.pollalgorand.rest.adapter.service.UnsignedASCTransactionService;
import com.pollalgorand.rest.domain.model.BlockchainPoll;
import com.pollalgorand.rest.domain.model.Poll;
import com.pollalgorand.rest.domain.repository.BlockchainPollRepository;
import com.pollalgorand.rest.domain.repository.PollRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CreatePollUseCase {

  private Logger logger = LoggerFactory.getLogger(CreatePollUseCase.class);

  private final BlockchainPollRepository blockChainBlockchainPollRepository;
  private final PollRepository postgresRepository;
  private UnsignedASCTransactionService unsignedASCTransactionService;
  private AccountCreatorService accountCreatorService;

  public CreatePollUseCase(BlockchainPollRepository blockChainBlockchainPollRepository,
      PollRepository postgresRepository,
      UnsignedASCTransactionService unsignedASCTransactionService,
      AccountCreatorService accountCreatorService) {

    this.blockChainBlockchainPollRepository = blockChainBlockchainPollRepository;
    this.postgresRepository = postgresRepository;
    this.unsignedASCTransactionService = unsignedASCTransactionService;
    this.accountCreatorService = accountCreatorService;
  }

  public BlockchainPoll create(Poll poll) {

    BlockchainPoll blockchainPoll = blockChainBlockchainPollRepository.save(poll);

    logger.info(String.format("Created poll with app id: %s", blockchainPoll.getAppId()));

    postgresRepository.save(blockchainPoll);

    return blockchainPoll;
  }

  public Transaction createUnsignedTx(Poll poll) {

    Account account = accountCreatorService.createAccountFrom(poll.getMnemonicKey());

    return unsignedASCTransactionService.createUnsignedTxFor(poll, account);
  }

}
