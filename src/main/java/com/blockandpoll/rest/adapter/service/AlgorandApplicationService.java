package com.blockandpoll.rest.adapter.service;

import static com.blockandpoll.rest.adapter.AlgorandUtils.headers;
import static com.blockandpoll.rest.adapter.AlgorandUtils.values;

import com.algorand.algosdk.v2.client.common.AlgodClient;
import com.algorand.algosdk.v2.client.model.PendingTransactionResponse;
import com.blockandpoll.rest.adapter.exceptions.RetrievingApplicationIdException;

public class AlgorandApplicationService {

  private final AlgodClient algodClient;

  public AlgorandApplicationService(AlgodClient algodClient) {
    this.algodClient = algodClient;
  }

  public Long getApplicationId(String transactionId) {
    PendingTransactionResponse pTrx;
    try {
      pTrx = algodClient.PendingTransactionInformation(transactionId)
          .execute(headers, values).body();
    } catch (Exception e) {
      throw new RetrievingApplicationIdException(e, transactionId);
    }
    return pTrx.applicationIndex;
  }
}