package com.pollalgorand.rest.adapter.repository;

import com.algorand.algosdk.v2.client.common.AlgodClient;
import com.algorand.algosdk.v2.client.model.PendingTransactionResponse;
import com.pollalgorand.rest.adapter.AlgorandUtils;
import com.pollalgorand.rest.adapter.exceptions.RetrievingApplicationIdException;

public class AlgorandApplicationService {

  private final AlgodClient algodClient;

  public AlgorandApplicationService(AlgodClient algodClient) {
    this.algodClient = algodClient;
  }

  public Long getApplicationId(String transactionId) {
    PendingTransactionResponse pTrx;
    try {
      pTrx = algodClient.PendingTransactionInformation(transactionId)
          .execute(AlgorandUtils.headers, AlgorandUtils.values).body();
    } catch (Exception e) {
      throw new RetrievingApplicationIdException(e, transactionId);
    }
    return pTrx.applicationIndex;
  }
}