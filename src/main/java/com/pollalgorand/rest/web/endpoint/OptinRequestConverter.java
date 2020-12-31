package com.pollalgorand.rest.web.endpoint;

import com.pollalgorand.rest.adapter.service.AccountCreatorService;
import com.pollalgorand.rest.domain.request.OptinAppRequest;
import com.pollalgorand.rest.web.request.OptinRequest;

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
