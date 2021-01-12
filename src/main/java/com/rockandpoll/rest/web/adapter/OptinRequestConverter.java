package com.rockandpoll.rest.web.adapter;

import com.rockandpoll.rest.adapter.service.AccountCreatorService;
import com.rockandpoll.rest.domain.request.OptinAppRequest;
import com.rockandpoll.rest.web.request.OptinRequest;

public class OptinRequestConverter {

  private AccountCreatorService accountCreatorService;

  public OptinRequestConverter(AccountCreatorService accountCreatorService) {

    this.accountCreatorService = accountCreatorService;
  }

  public OptinAppRequest fromRequestToDomain(long appId, OptinRequest optinRequest) {
    return new OptinAppRequest(appId,
        accountCreatorService.createAccountFrom(optinRequest.getMnemonicKey()));
  }
}
