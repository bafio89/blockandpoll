package com.pollalgorand.rest.adapter.service;

import static java.util.Arrays.asList;

import com.algorand.algosdk.crypto.TEALProgram;
import com.algorand.algosdk.logic.StateSchema;
import com.algorand.algosdk.transaction.Transaction;
import com.pollalgorand.rest.adapter.PollTealParams;
import com.pollalgorand.rest.adapter.exceptions.BlockChainParameterException;
import com.pollalgorand.rest.adapter.exceptions.InvalidSenderAddressException;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BuildApplicationCreateTransactionService {

  public static final int STATIC_GLOBAL_VARIABLES_NUMBER = 4;
  private Logger logger = LoggerFactory.getLogger(BuildApplicationCreateTransactionService.class);

  private BlockchainParameterService blockchainParameterService;

  public BuildApplicationCreateTransactionService(
      BlockchainParameterService blockchainParameterService) {
    this.blockchainParameterService = blockchainParameterService;
  }

  public Transaction buildTransaction(PollTealParams pollTealParams,
      TEALProgram approvalProgram, TEALProgram clearStateProgram, String sender) {
    Transaction transaction;
    try {
      transaction = Transaction.ApplicationCreateTransactionBuilder()
          .sender(sender)
          .args(arguments(pollTealParams))
          .suggestedParams(blockchainParameterService.getParameters())
          .approvalProgram(approvalProgram)
          .clearStateProgram(clearStateProgram)
          .globalStateSchema(new StateSchema(
              STATIC_GLOBAL_VARIABLES_NUMBER + pollTealParams.getOptions().size(), 1))
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
