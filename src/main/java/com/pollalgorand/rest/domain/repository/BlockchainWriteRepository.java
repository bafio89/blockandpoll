package com.pollalgorand.rest.domain.repository;

import com.pollalgorand.rest.domain.request.OptinAppRequest;
import com.pollalgorand.rest.domain.request.VoteAppRequest;

public interface BlockchainWriteRepository {

  void optin(OptinAppRequest optinAppRequest);

  void vote(VoteAppRequest voteAppRequest);
}
