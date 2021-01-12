package com.blockandpoll.rest.web.adapter;

import com.blockandpoll.rest.adapter.service.AccountCreatorService;
import com.blockandpoll.rest.domain.request.OptinAppRequest;
import com.blockandpoll.rest.web.request.OptinRequest;

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
