package com.pollalgorand.rest;

import static org.junit.Assert.*;

import com.algorand.algosdk.algod.client.api.AlgodApi;
import com.algorand.algosdk.builder.transaction.ApplicationCallTransactionBuilder;
import com.algorand.algosdk.transaction.Transaction;
import com.algorand.algosdk.transaction.Transaction.Type;
import org.junit.Test;

public class AlgorandPollRepositoryTest {

  private AlgodApi algodApi;
  private String srcAccount;

  private AlgorandPollRepository algorandPollRepository = new AlgorandPollRepository(algodApi, srcAccount);


  @Test
  public void submitATransaction() {

    Transaction appplicationCallTransaction = ApplicationCallTransactionBuilder.Builder().build();
  }
}