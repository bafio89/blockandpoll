package com.pollalgorand.rest.domain.usecase;

import com.pollalgorand.rest.domain.model.BlockchainPoll;
import com.pollalgorand.rest.domain.repository.PollRepository;
import java.util.List;

public class RetrievePollUseCase {

  private PollRepository showPollRepository;

  public RetrievePollUseCase(
      PollRepository showPollRepository) {
    this.showPollRepository = showPollRepository;
  }

  public List<BlockchainPoll> retrievePolls() {
    return showPollRepository.find();
  }
}
