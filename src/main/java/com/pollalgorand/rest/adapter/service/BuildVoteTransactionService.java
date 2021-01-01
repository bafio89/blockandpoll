package com.pollalgorand.rest.adapter.service;

import com.algorand.algosdk.account.Account;
import com.algorand.algosdk.transaction.Transaction;
import com.pollalgorand.rest.domain.request.VoteAppRequest;

public class BuildVoteTransactionService {

  private BlockchainParameterService blockchainParameterService;

  public BuildVoteTransactionService(
      BlockchainParameterService blockchainParameterService) {
    this.blockchainParameterService = blockchainParameterService;
  }

  public Transaction buildTransaction(Account account, VoteAppRequest optinAppRequest) {
    return null;
  }
}
