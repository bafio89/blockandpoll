package com.pollalgorand.rest.adapter.service;

import static com.pollalgorand.rest.adapter.AlgorandUtils.txHeaders;
import static com.pollalgorand.rest.adapter.AlgorandUtils.txValues;

import com.algorand.algosdk.v2.client.common.AlgodClient;

public class TransactionSenderService {

  private AlgodClient algodClient;

  public TransactionSenderService(AlgodClient algodClient) {
    this.algodClient = algodClient;
  }

  public String send(byte[] encodedTxBytes) throws Exception {
    return algodClient.RawTransaction().rawtxn(encodedTxBytes).execute(txHeaders, txValues)
        .body().txId;
  }
}
