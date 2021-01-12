package com.rockandpoll.rest.adapter.service;

import static java.nio.charset.StandardCharsets.UTF_8;
import static java.util.Arrays.asList;

import com.algorand.algosdk.transaction.Transaction;
import com.rockandpoll.rest.domain.request.VoteAppRequest;
import java.util.List;

public class BuildVoteTransactionService {

  private BlockchainParameterService blockchainParameterService;

  public BuildVoteTransactionService(
      BlockchainParameterService blockchainParameterService) {
    this.blockchainParameterService = blockchainParameterService;
  }

  public Transaction buildTransaction(VoteAppRequest voteAppRequest) {
    return Transaction.ApplicationCallTransactionBuilder()
        .suggestedParams(blockchainParameterService.getParameters())
        .sender(voteAppRequest.getAccount().getAddress())
        .args(arguments(voteAppRequest.getSelectedOption()))
        .applicationId(voteAppRequest.getAppId()).build();
  }

  private List<byte[]> arguments(String selectedOption){
    return asList("vote".getBytes(UTF_8),
        String.format("%s" ,selectedOption).getBytes(UTF_8));
  }
}
