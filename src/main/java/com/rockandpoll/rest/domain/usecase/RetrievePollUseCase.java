package com.rockandpoll.rest.domain.usecase;

import com.rockandpoll.rest.domain.exceptions.PollNotFoundException;
import com.rockandpoll.rest.domain.model.ApplicationInfoFromBlockchain;
import com.rockandpoll.rest.domain.model.BlockchainPoll;
import com.rockandpoll.rest.domain.model.EnrichedBlockchainPoll;
import com.rockandpoll.rest.domain.repository.BlockchainReadRepository;
import com.rockandpoll.rest.domain.repository.PollRepository;
import java.util.List;
import java.util.Optional;

public class RetrievePollUseCase {

  private PollRepository showPollRepository;
  private BlockchainReadRepository blockchainReadRepository;

  public RetrievePollUseCase(
      PollRepository showPollRepository,
      BlockchainReadRepository blockchainReadRepository) {
    this.showPollRepository = showPollRepository;
    this.blockchainReadRepository = blockchainReadRepository;
  }

  public List<BlockchainPoll> retrievePolls() {
    return showPollRepository.find();
  }

  public EnrichedBlockchainPoll findPollByAppId(long appId) {

    Optional<BlockchainPoll> blockchainPoll = showPollRepository.findBy(appId);
    ApplicationInfoFromBlockchain applicationInfoFromBlockchain = blockchainPoll
        .map(poll -> blockchainReadRepository.findApplicationInfoBy(poll))
        .orElseThrow(() -> new PollNotFoundException(appId));

    return new EnrichedBlockchainPoll(blockchainPoll.get(), applicationInfoFromBlockchain);
  }
}
