package com.pollalgorand.rest;

import com.algorand.algosdk.account.Account;
import com.algorand.algosdk.builder.transaction.ApplicationCallTransactionBuilder;
import com.algorand.algosdk.crypto.Address;
import com.algorand.algosdk.crypto.TEALProgram;
import com.algorand.algosdk.logic.StateSchema;
import com.algorand.algosdk.transaction.Transaction;
import com.algorand.algosdk.v2.client.common.AlgodClient;
import org.junit.Test;

public class AlgorandBlockchainPollRepositoryTest {

  private AlgodClient algodClient;
  private String srcAccount;

  private AlgorandPollRepository algorandPollRepository = new AlgorandPollRepository(algodClient, srcAccount);


  @Test
  public void submitATransaction() throws Exception {


    TEALProgram tealProgram = new TEALProgram();

    Account creatorAccount = new Account("a mnemonic string");
    Address sender = creatorAccount.getAddress();
    Transaction txn = Transaction.ApplicationCreateTransactionBuilder()
        .sender(sender)
        .suggestedParams(algodClient.TransactionParams().execute().body())
        .approvalProgram(tealProgram)
        .clearStateProgram(null)
        .globalStateSchema(new StateSchema(1, 0))
        .localStateSchema(new StateSchema(1, 1))
        .build();
//    Transaction applicationCallTransaction = Transaction.ApplicationCreateTransactionBuilder().approvalProgram().build();

  }
}