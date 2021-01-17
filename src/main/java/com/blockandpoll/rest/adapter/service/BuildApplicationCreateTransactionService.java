package com.blockandpoll.rest.adapter.service;

import static java.util.Arrays.asList;

import com.algorand.algosdk.crypto.TEALProgram;
import com.algorand.algosdk.logic.StateSchema;
import com.algorand.algosdk.transaction.Transaction;
import com.blockandpoll.rest.adapter.PollTealParams;
import com.blockandpoll.rest.adapter.exceptions.BlockChainParameterException;
import com.blockandpoll.rest.adapter.exceptions.InvalidSenderAddressException;
import com.google.common.primitives.Bytes;
import java.nio.charset.StandardCharsets;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BuildApplicationCreateTransactionService {

  public static final int STATIC_GLOBAL_VARIABLES_NUMBER = 4;
  public static final String APPLICATION_TAG = "[blockandpoll][permissionless] ";

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
          .note(Bytes.concat(APPLICATION_TAG.getBytes(StandardCharsets.UTF_8), pollTealParams.getQuestion()))
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
      throw new InvalidSenderAddressException(e.getMessage());
    } catch (Exception e) {
      logger.error("Something goes wrong getting blockchain parameters transaction", e);
      throw new BlockChainParameterException(e.getMessage());
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
