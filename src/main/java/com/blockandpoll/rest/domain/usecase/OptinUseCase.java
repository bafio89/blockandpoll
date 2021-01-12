package com.blockandpoll.rest.domain.usecase;

import com.blockandpoll.rest.domain.DateValidator;
import com.blockandpoll.rest.domain.exceptions.OptinAlreadyDoneException;
import com.blockandpoll.rest.domain.exceptions.OptinIntervalTimeException;
import com.blockandpoll.rest.domain.exceptions.PollNotFoundException;
import com.blockandpoll.rest.domain.model.BlockchainPoll;
import com.blockandpoll.rest.domain.repository.BlockchainReadRepository;
import com.blockandpoll.rest.domain.repository.BlockchainWriteRepository;
import com.blockandpoll.rest.domain.repository.PollRepository;
import com.blockandpoll.rest.domain.request.OptinAppRequest;
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

    blockchainPoll.map(poll -> isNowInSubscriptionDate(optinAppRequest, poll))
        .orElseThrow(() -> new PollNotFoundException(optinAppRequest.getAppId()));

    if (blockChainReadRepository.isAccountSubscribedTo(optinAppRequest)) {
      throw new OptinAlreadyDoneException(optinAppRequest.getAppId());
    }

    blockchainWriteRepository.optin(optinAppRequest);
  }

  private boolean isNowInSubscriptionDate(OptinAppRequest optinAppRequest,
      BlockchainPoll blockchainPoll) {
    if (!dateValidator.isNowInInterval(blockchainPoll.getStartSubscriptionTime(),
            blockchainPoll.getEndSubscriptionTime())) {
      throw new OptinIntervalTimeException(optinAppRequest.getAppId());
    }
    return true;
  }
}
