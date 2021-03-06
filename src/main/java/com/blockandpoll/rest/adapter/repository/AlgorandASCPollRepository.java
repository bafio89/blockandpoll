package com.blockandpoll.rest.adapter.repository;

import com.algorand.algosdk.account.Account;
import com.algorand.algosdk.transaction.Transaction;
import com.algorand.algosdk.v2.client.common.AlgodClient;
import com.blockandpoll.rest.adapter.converter.PollBlockchainAdapter;
import com.blockandpoll.rest.adapter.exceptions.InvalidMnemonicKeyException;
import com.blockandpoll.rest.adapter.service.AccountCreatorService;
import com.blockandpoll.rest.adapter.service.AlgorandApplicationService;
import com.blockandpoll.rest.adapter.service.TransactionWriterService;
import com.blockandpoll.rest.adapter.service.UnsignedASCTransactionService;
import com.blockandpoll.rest.domain.model.BlockchainPoll;
import com.blockandpoll.rest.domain.model.Poll;
import com.blockandpoll.rest.domain.repository.BlockchainPollRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AlgorandASCPollRepository implements BlockchainPollRepository {

  private Logger logger = LoggerFactory.getLogger(AlgorandASCPollRepository.class);

  private final AlgorandApplicationService algorandApplicationService;
  private final TransactionWriterService transactionWriterService;
  private final AccountCreatorService accountCreatorService;
  private final PollBlockchainAdapter pollBlockchainAdapter;
  private final UnsignedASCTransactionService unsignedASCTransactionService;

  private AlgodClient algodClient;

  public AlgorandASCPollRepository(AlgodClient algodClient,
      AccountCreatorService accountCreatorService,
      UnsignedASCTransactionService unsignedASCTransactionService,
      PollBlockchainAdapter pollBlockchainAdapter,
      AlgorandApplicationService algorandApplicationService,
      TransactionWriterService transactionWriterService) {

    this.algodClient = algodClient;
    this.accountCreatorService = accountCreatorService;
    this.unsignedASCTransactionService = unsignedASCTransactionService;
    this.pollBlockchainAdapter = pollBlockchainAdapter;
    this.algorandApplicationService = algorandApplicationService;
    this.transactionWriterService = transactionWriterService;
  }

  @Override
  public BlockchainPoll save(Poll poll) {

    try {
      Account account = accountCreatorService.createAccountFrom(poll.getMnemonicKey());

      Transaction unsignedTx = unsignedASCTransactionService.createUnsignedTxFor(poll, account);

      String transactionId = transactionWriterService.write(account, unsignedTx);

      Long appId = algorandApplicationService.getApplicationId(transactionId);

      return pollBlockchainAdapter.fromPollToBlockchainPoll(poll, appId);

    } catch (IllegalArgumentException e) {
      logger
          .error("Something gone wrong creating account from mnemonic key creating poll {}.", poll,
              e);
      throw new InvalidMnemonicKeyException(e.getMessage());
    }

  }

}
