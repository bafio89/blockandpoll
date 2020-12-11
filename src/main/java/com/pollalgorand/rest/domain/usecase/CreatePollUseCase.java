package com.pollalgorand.rest.domain.usecase;


import com.algorand.algosdk.transaction.Transaction;
import com.pollalgorand.rest.domain.model.BlockchainPoll;
import com.pollalgorand.rest.domain.model.Poll;
import com.pollalgorand.rest.domain.repository.BlockchainPollRepository;
import com.pollalgorand.rest.domain.repository.PollRepository;
import java.util.Optional;

public class CreatePollUseCase {

  private final BlockchainPollRepository blockChainBlockchainPollRepository;
  private final PollRepository postgresRepository;

  public CreatePollUseCase(BlockchainPollRepository blockChainBlockchainPollRepository,
      PollRepository postgresRepository) {

    this.blockChainBlockchainPollRepository = blockChainBlockchainPollRepository;
    this.postgresRepository = postgresRepository;
  }

  public Optional<BlockchainPoll> create(Poll poll) {

    Optional<BlockchainPoll> blockchainPoll = blockChainBlockchainPollRepository.save(poll);

    blockchainPoll.ifPresent(postgresRepository::save);

    return blockchainPoll;
  }

  public Transaction createUnsignedTx(Poll poll) {

    return blockChainBlockchainPollRepository.createUnsignedTxFor(poll);
  }

}
