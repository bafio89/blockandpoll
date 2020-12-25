package com.pollalgorand.rest.adapter.service;

import static com.pollalgorand.rest.adapter.AlgorandUtils.headers;
import static com.pollalgorand.rest.adapter.AlgorandUtils.values;
import static java.util.Arrays.asList;

import com.algorand.algosdk.crypto.TEALProgram;
import com.algorand.algosdk.logic.StateSchema;
import com.algorand.algosdk.transaction.Transaction;
import com.algorand.algosdk.v2.client.common.AlgodClient;
import com.pollalgorand.rest.adapter.PollTealParams;
import com.pollalgorand.rest.adapter.exceptions.BlockChainParameterException;
import com.pollalgorand.rest.adapter.exceptions.InvalidSenderAddressException;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BuildTransactionService {

  private Logger logger = LoggerFactory.getLogger(BuildTransactionService.class);

  private AlgodClient algodClient;

  public BuildTransactionService(AlgodClient algodClient) {
    this.algodClient = algodClient;
  }

  public Transaction buildTransaction(PollTealParams pollTealParams,
      TEALProgram approvalProgram, TEALProgram clearStateProgram, String sender) {
    Transaction transaction;
    try {
      transaction = Transaction.ApplicationCreateTransactionBuilder()
          .sender(sender)
          .args(arguments(pollTealParams))
          .suggestedParams(algodClient.TransactionParams().execute(headers, values).body())
          .approvalProgram(approvalProgram)
          .clearStateProgram(clearStateProgram)
          .globalStateSchema(new StateSchema(6, 1))
          .localStateSchema(new StateSchema(0, 1))
          .build();
    } catch (IllegalArgumentException e) {
      logger.error("Something goes wrong with Sender Address transaction", e);
      throw new InvalidSenderAddressException(e);
    } catch (Exception e) {
      logger.error("Something goes wrong getting blockchain parameters transaction", e);
      throw new BlockChainParameterException(e);
    }
    return transaction;
  }

  private List<byte[]> arguments(PollTealParams pollTealParams) {

    return asList(pollTealParams.getStartSubscriptionTime(),
        pollTealParams.getEndSubscriptionTime(),
        pollTealParams.getStartVotingTime(),
        pollTealParams.getEndVotingTime());
  }
}
