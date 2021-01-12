package com.blockandpoll.rest.web.adapter;

import com.blockandpoll.rest.adapter.service.AccountCreatorService;
import com.blockandpoll.rest.domain.request.VoteAppRequest;
import com.blockandpoll.rest.web.request.VoteRequest;

public class VoteRequestConverter {

  private AccountCreatorService accountCreatorService;

  public VoteRequestConverter(AccountCreatorService accountCreatorService) {

    this.accountCreatorService = accountCreatorService;
  }

  public VoteAppRequest fromRequestToDomain(long appId, VoteRequest voteRequest) {

    return new VoteAppRequest(appId, accountCreatorService.createAccountFrom(voteRequest.getMnemonicKey()), voteRequest.getSelectedOption());
  }
}
