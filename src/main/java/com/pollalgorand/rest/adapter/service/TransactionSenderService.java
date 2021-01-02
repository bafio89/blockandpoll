package com.pollalgorand.rest.adapter.service;

import static com.pollalgorand.rest.adapter.AlgorandUtils.txHeaders;
import static com.pollalgorand.rest.adapter.AlgorandUtils.txValues;

import com.algorand.algosdk.v2.client.common.AlgodClient;
import com.algorand.algosdk.v2.client.common.Response;
import com.algorand.algosdk.v2.client.model.PostTransactionsResponse;
import com.pollalgorand.rest.adapter.exceptions.AlgorandInteractionError;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TransactionSenderService {

  private Logger logger = LoggerFactory.getLogger(TransactionSenderService.class);

  private AlgodClient algodClient;

  public TransactionSenderService(AlgodClient algodClient) {
    this.algodClient = algodClient;
  }

  public String send(byte[] encodedTxBytes) {
    Response<PostTransactionsResponse> response;

    try {
      response = algodClient.RawTransaction().rawtxn(encodedTxBytes)
          .execute(txHeaders, txValues);
    } catch (Exception e) {
      throw  new AlgorandInteractionError(e.getMessage());
    }

    if(response.code() == 200){
      return response.body().txId;
    }

    logger.error("Sending transaction to algorand blockchain response has code {}. Message {}", response.code(), response.message());
    throw new AlgorandInteractionError(response.code(), response.message());

  }
}
