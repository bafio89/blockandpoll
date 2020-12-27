package com.pollalgorand.rest.domain.usecase;

import com.pollalgorand.rest.domain.DateValidator;
import com.pollalgorand.rest.domain.OptinAppRequest;
import com.pollalgorand.rest.domain.exceptions.OptinAlreadDoneException;
import com.pollalgorand.rest.domain.model.BlockchainPoll;
import com.pollalgorand.rest.domain.repository.BlockchainReadRepository;
import com.pollalgorand.rest.domain.repository.BlockchainWriteRepository;
import com.pollalgorand.rest.domain.repository.PollRepository;

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

    BlockchainPoll blockchainPoll = pollRepository.findBy(optinAppRequest.getAppId());

    if (!dateValidator.isOptinOpen(blockchainPoll)){
      throw new OptinIntervalTimeException(optinAppRequest.getAppId());
    }

    if (!blockChainReadRepository.isOptinAllowedFor(optinAppRequest)) {
      throw new OptinAlreadDoneException(optinAppRequest.getAppId(), optinAppRequest.getSender());
    }

    blockchainWriteRepository.optin(optinAppRequest);
  }
}
