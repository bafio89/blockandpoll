package com.pollalgorand.rest;

import com.algorand.algosdk.builder.transaction.ApplicationCallTransactionBuilder;
import com.algorand.algosdk.transaction.Transaction;
import com.algorand.algosdk.v2.client.common.AlgodClient;
import org.junit.Test;

public class AlgorandBlockchainPollRepositoryTest {

  private AlgodClient algodClient;
  private String srcAccount;

  private AlgorandPollRepository algorandPollRepository = new AlgorandPollRepository(algodClient, srcAccount);


  @Test
  public void submitATransaction() {

//    Transaction applicationCallTransaction = Transaction.ApplicationCreateTransactionBuilder().approvalProgram().build();

  }
}