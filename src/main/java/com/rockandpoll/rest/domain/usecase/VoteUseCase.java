package com.rockandpoll.rest.domain.usecase;

import com.rockandpoll.rest.domain.DateValidator;
import com.rockandpoll.rest.domain.exceptions.AlreadyVotedException;
import com.rockandpoll.rest.domain.exceptions.OptinIntervalTimeException;
import com.rockandpoll.rest.domain.exceptions.PollNotFoundException;
import com.rockandpoll.rest.domain.exceptions.VoteIntervalTimeException;
import com.rockandpoll.rest.domain.model.BlockchainPoll;
import com.rockandpoll.rest.domain.repository.BlockchainReadRepository;
import com.rockandpoll.rest.domain.repository.BlockchainWriteRepository;
import com.rockandpoll.rest.domain.repository.PollRepository;
import com.rockandpoll.rest.domain.request.OptinAppRequest;
import com.rockandpoll.rest.domain.request.VoteAppRequest;
import java.util.Optional;

public class VoteUseCase {

  public static final int API_TIME_DELAY = 500;
  private final PollRepository pollRepository;
  private final DateValidator dateValidator;
  private final BlockchainReadRepository algorandReadRepository;
  private final BlockchainWriteRepository blockchainWriteRepository;

  public VoteUseCase(PollRepository pollRepository, DateValidator dateValidator,
      BlockchainReadRepository algorandReadRepository,
      BlockchainWriteRepository blockchainWriteRepository) {

    this.pollRepository = pollRepository;
    this.dateValidator = dateValidator;
    this.algorandReadRepository = algorandReadRepository;
    this.blockchainWriteRepository = blockchainWriteRepository;
  }

  public void vote(VoteAppRequest voteAppRequest) {

    Optional<BlockchainPoll> blockchainPoll = pollRepository.findBy(voteAppRequest.getAppId());

    blockchainPoll.map(poll -> validateVotingDate(voteAppRequest, poll))
        .orElseThrow(() -> new PollNotFoundException(voteAppRequest.getAppId()));

    if(algorandReadRepository.hasAddressAlreadyVotedFor(voteAppRequest.getAppId(), voteAppRequest.getAccount().getAddress())){
      throw new AlreadyVotedException(voteAppRequest.getAccount().getAddress().toString(), voteAppRequest.getAppId());
    }

    OptinAppRequest optinAppRequest = new OptinAppRequest(voteAppRequest.getAppId(),
        voteAppRequest.getAccount());

    if(!algorandReadRepository.isAccountSubscribedTo(optinAppRequest)){
      blockchainPoll.map(poll -> validateSubscriptionDate(voteAppRequest, poll));

      // Introduced because of the limit imposed by purestake.
      // In an ipotetical production environment this problem should not exist
      try {
        Thread.sleep(API_TIME_DELAY);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
      blockchainWriteRepository.optin(optinAppRequest);
    }

    blockchainWriteRepository.vote(voteAppRequest);
  }

  private boolean validateSubscriptionDate(VoteAppRequest voteAppRequest,
      BlockchainPoll blockchainPoll) {
    if (!dateValidator.isNowInInterval(blockchainPoll.getStartSubscriptionTime(),
        blockchainPoll.getEndSubscriptionTime())) {
      throw new OptinIntervalTimeException(voteAppRequest.getAppId());
    }
    return true;
  }

  private boolean validateVotingDate(VoteAppRequest voteAppRequest,
      BlockchainPoll blockchainPoll) {
    if (!dateValidator.isNowInInterval(blockchainPoll.getStartVotingTime(),
        blockchainPoll.getEndVotingTime())) {
      throw new VoteIntervalTimeException(voteAppRequest.getAppId());
    }
    return true;
  }
}
