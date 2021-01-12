package com.blockandpoll.rest.domain.repository;

import com.blockandpoll.rest.domain.request.OptinAppRequest;
import com.blockandpoll.rest.domain.request.VoteAppRequest;

public interface BlockchainWriteRepository {

  void optin(OptinAppRequest optinAppRequest);

  void vote(VoteAppRequest voteAppRequest);
}