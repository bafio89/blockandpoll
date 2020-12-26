package com.pollalgorand.rest.adapter.service;

import com.algorand.algosdk.account.Account;
import com.algorand.algosdk.transaction.SignedTransaction;
import com.algorand.algosdk.transaction.Transaction;
import com.algorand.algosdk.util.Encoder;
import com.fasterxml.jackson.core.JsonProcessingException;
import java.security.GeneralSecurityException;

public class TransactionSignerService {

  public byte[] sign(Transaction unsignedTx, String mnemonicKey)
      throws GeneralSecurityException, JsonProcessingException {

    Account account = new Account(mnemonicKey);

    SignedTransaction signedTx = account.signTransaction(unsignedTx);

    return Encoder.encodeToMsgPack(signedTx);
  }
}