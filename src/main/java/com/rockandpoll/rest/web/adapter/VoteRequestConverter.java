package com.rockandpoll.rest.web.adapter;

import com.rockandpoll.rest.adapter.service.AccountCreatorService;
import com.rockandpoll.rest.domain.request.VoteAppRequest;
import com.rockandpoll.rest.web.request.VoteRequest;

public class VoteRequestConverter {

  private AccountCreatorService accountCreatorService;

  public VoteRequestConverter(AccountCreatorService accountCreatorService) {

    this.accountCreatorService = accountCreatorService;
  }

  public VoteAppRequest fromRequestToDomain(long appId, VoteRequest voteRequest) {

    return new VoteAppRequest(appId, accountCreatorService.createAccountFrom(voteRequest.getMnemonicKey()), voteRequest.getSelectedOption());
  }
}
