package com.pollalgorand.rest.adapter.service;

import static com.pollalgorand.rest.adapter.AlgorandUtils.headers;
import static com.pollalgorand.rest.adapter.AlgorandUtils.values;

import com.algorand.algosdk.v2.client.common.AlgodClient;
import com.algorand.algosdk.v2.client.model.TransactionParametersResponse;

public class BlockchainParameterService {

  private AlgodClient algodClient;

  public BlockchainParameterService(AlgodClient algodClient) {

    this.algodClient = algodClient;
  }

  public TransactionParametersResponse getParameters() {
    try {
      return algodClient.TransactionParams().execute(headers, values).body();
    } catch (Exception e) {
      e.printStackTrace();
      throw new RuntimeException();
    }
  }
}
