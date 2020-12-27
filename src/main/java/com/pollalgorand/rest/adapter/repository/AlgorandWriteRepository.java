package com.pollalgorand.rest.adapter.repository;

import com.algorand.algosdk.v2.client.common.AlgodClient;
import com.pollalgorand.rest.adapter.service.BuildOptinTransactionService;
import com.pollalgorand.rest.adapter.service.TransactionConfirmationService;
import com.pollalgorand.rest.adapter.service.TransactionSignerService;
import com.pollalgorand.rest.domain.OptinAppRequest;
import com.pollalgorand.rest.domain.repository.BlockchainWriteRepository;

public class AlgorandWriteRepository implements BlockchainWriteRepository {

  private AlgodClient algodClient;

  public AlgorandWriteRepository(AlgodClient algodClient,
      BuildOptinTransactionService buildOptinTransactionService,
      TransactionSignerService transactionSignerService,
      TransactionConfirmationService transactionConfirmationService) {

    this.algodClient = algodClient;
  }

  @Override
  public void optin(OptinAppRequest optinAppRequest) {

//    Transaction.ApplicationOptInTransactionBuilder()
//        .sender(optinAppRequest.getSender())
//        .args().build();
  }
}
