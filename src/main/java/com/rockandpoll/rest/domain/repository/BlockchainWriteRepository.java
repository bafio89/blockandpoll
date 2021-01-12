package com.rockandpoll.rest.domain.repository;

import com.rockandpoll.rest.domain.request.OptinAppRequest;
import com.rockandpoll.rest.domain.request.VoteAppRequest;

public interface BlockchainWriteRepository {

  void optin(OptinAppRequest optinAppRequest);

  void vote(VoteAppRequest voteAppRequest);
}