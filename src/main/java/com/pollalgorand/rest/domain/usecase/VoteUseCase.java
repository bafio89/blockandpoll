package com.pollalgorand.rest.domain.usecase;

import com.pollalgorand.rest.domain.DateValidator;
import com.pollalgorand.rest.domain.exceptions.AlreadyVotedException;
import com.pollalgorand.rest.domain.exceptions.PollNotFoundException;
import com.pollalgorand.rest.domain.model.BlockchainPoll;
import com.pollalgorand.rest.domain.repository.BlockchainReadRepository;
import com.pollalgorand.rest.domain.repository.BlockchainWriteRepository;
import com.pollalgorand.rest.domain.repository.PollRepository;
import com.pollalgorand.rest.domain.request.OptinAppRequest;
import com.pollalgorand.rest.domain.request.VoteAppRequest;
import java.util.Optional;

public class VoteUseCase {

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

    OptinAppRequest optinAppRequest = new OptinAppRequest(voteAppRequest.getAppId(),
        voteAppRequest.getAccount());

    if(algorandReadRepository.hasAddressAlreadyVotedFor(voteAppRequest.getAppId(), voteAppRequest.getAccount().getAddress())){
      throw new AlreadyVotedException(voteAppRequest.getAccount().getAddress().toString(), voteAppRequest.getAppId());
    }

    if(!algorandReadRepository.isAccountSubscribedTo(optinAppRequest)){
      blockchainPoll.map(poll -> validateSubscriptionDate(voteAppRequest, poll));
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
