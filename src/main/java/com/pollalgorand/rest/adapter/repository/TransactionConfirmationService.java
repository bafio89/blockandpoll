package com.pollalgorand.rest.adapter.repository;

import static com.pollalgorand.rest.adapter.AlgorandUtils.headers;
import static com.pollalgorand.rest.adapter.AlgorandUtils.values;

import com.algorand.algosdk.v2.client.common.AlgodClient;
import com.algorand.algosdk.v2.client.common.Response;
import com.algorand.algosdk.v2.client.model.PendingTransactionResponse;
import com.pollalgorand.rest.adapter.exceptions.WaitingTransactionConfirmationException;

public class TransactionConfirmationService {

  private final AlgodClient algodClient;

  public TransactionConfirmationService(
      AlgodClient algodClient) {
    this.algodClient = algodClient;
  }

  void waitForConfirmation(String txID) {

    try {

      Long lastRound = this.algodClient.GetStatus().execute(headers, values)
          .body().lastRound;
      while (true) {

        // Check the pending transactions
        Response<PendingTransactionResponse> pendingInfo = algodClient
            .PendingTransactionInformation(txID)
            .execute(headers, values);
        if (pendingInfo.body().confirmedRound != null && pendingInfo.body().confirmedRound > 0) {
          // Got the completed Transaction
          System.out.println(
              "Transaction " + txID + " confirmed in round " + pendingInfo.body().confirmedRound);
          break;
        }
        lastRound++;
        algodClient.WaitForBlock(lastRound).execute(headers, values);
      }
    } catch (Exception e) {
      throw new WaitingTransactionConfirmationException(e, txID);
    }
  }
}