package com.pollalgorand.rest.domain.usecase;

import com.pollalgorand.rest.domain.DateValidator;
import com.pollalgorand.rest.domain.exceptions.OptinAlreadyDoneException;
import com.pollalgorand.rest.domain.model.BlockchainPoll;
import com.pollalgorand.rest.domain.repository.BlockchainReadRepository;
import com.pollalgorand.rest.domain.repository.BlockchainWriteRepository;
import com.pollalgorand.rest.domain.repository.PollRepository;
import com.pollalgorand.rest.domain.request.OptinAppRequest;
import java.util.Optional;

public class OptinUseCase {

  private final BlockchainReadRepository blockChainReadRepository;
  private final BlockchainWriteRepository blockchainWriteRepository;
  private final PollRepository pollRepository;
  private final DateValidator dateValidator;

  public OptinUseCase(BlockchainReadRepository blockChainReadRepository,
      BlockchainWriteRepository blockchainWriteRepository,
      PollRepository pollRepository,
      DateValidator dateValidator) {

    this.blockChainReadRepository = blockChainReadRepository;
    this.blockchainWriteRepository = blockchainWriteRepository;
    this.pollRepository = pollRepository;
    this.dateValidator = dateValidator;
  }

  public void optin(OptinAppRequest optinAppRequest) {

    Optional<BlockchainPoll> blockchainPoll = pollRepository.findBy(optinAppRequest.getAppId());

    if (blockchainPoll.isPresent() && !dateValidator.isNowInInterval(blockchainPoll.get().getStartSubscriptionTime(),
        blockchainPoll.get().getEndSubscriptionTime())) {
      throw new OptinIntervalTimeException(optinAppRequest.getAppId());
    }

    if (blockChainReadRepository.isAccountSubscribedTo(optinAppRequest)) {
      throw new OptinAlreadyDoneException(optinAppRequest.getAppId());
    }

    blockchainWriteRepository.optin(optinAppRequest);
  }
}
