package com.pollalgorand.rest.adapter.service;

import com.algorand.algosdk.transaction.Transaction;
import com.pollalgorand.rest.domain.request.OptinAppRequest;

public class BuildOptinTransactionService{

  private BlockchainParameterService blockchainParameterService;

  public BuildOptinTransactionService(BlockchainParameterService blockchainParameterService) {

    this.blockchainParameterService = blockchainParameterService;
  }

  public Transaction buildTransaction(OptinAppRequest optinAppRequest) {
    return Transaction.ApplicationOptInTransactionBuilder()
        .suggestedParams(blockchainParameterService.getParameters())
        .sender(optinAppRequest.getAccount().getAddress())
        .applicationId(optinAppRequest.getAppId())
        .build();
  }
}
