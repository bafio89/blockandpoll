package com.pollalgorand.rest.adapter.service;

import com.algorand.algosdk.account.Account;
import com.algorand.algosdk.transaction.Transaction;
import com.pollalgorand.rest.domain.OptinAppRequest;

public class BuildOptinTransactionService {

  private BlockchainParameterService blockchainParameterService;

  public BuildOptinTransactionService(BlockchainParameterService blockchainParameterService) {

    this.blockchainParameterService = blockchainParameterService;
  }

  public Transaction buildTransaction(Account account, OptinAppRequest optinAppRequest) {
    return Transaction.ApplicationOptInTransactionBuilder()
        .suggestedParams(blockchainParameterService.getParameters())
        .sender(account.getAddress())
        .applicationId(optinAppRequest.getAppId())
        .build();
  }
}
