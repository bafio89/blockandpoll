package com.pollalgorand.rest.web.adapter;

import com.pollalgorand.rest.adapter.service.AccountCreatorService;
import com.pollalgorand.rest.domain.request.VoteAppRequest;
import com.pollalgorand.rest.web.request.VoteRequest;

public class VoteRequestConverter {

  private AccountCreatorService accountCreatorService;

  public VoteRequestConverter(AccountCreatorService accountCreatorService) {

    this.accountCreatorService = accountCreatorService;
  }

  public VoteAppRequest fromRequestToDomain(long appId, VoteRequest voteRequest) {

    return new VoteAppRequest(appId, accountCreatorService.createAccountFrom(voteRequest.getMnemonicKey()), voteRequest.getSelectedOption());
  }
}
