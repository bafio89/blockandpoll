package com.pollalgorand.rest.web.endpoint;

import com.pollalgorand.rest.adapter.exceptions.InvalidMnemonicKeyException;
import com.pollalgorand.rest.adapter.service.AccountCreatorService;
import com.pollalgorand.rest.domain.request.OptinAppRequest;
import com.pollalgorand.rest.web.request.OptinRequest;
import java.security.GeneralSecurityException;

public class OptinRequestConverter {

  private AccountCreatorService accountCreatorService;

  public OptinRequestConverter(AccountCreatorService accountCreatorService) {

    this.accountCreatorService = accountCreatorService;
  }

  public OptinAppRequest fromRequestToDomain(long appId, OptinRequest optinRequest) {
    try {
      return new OptinAppRequest(appId,
          accountCreatorService.createAccountFrom(optinRequest.getMnemonicKey()));
    } catch (GeneralSecurityException e) {
      throw new InvalidMnemonicKeyException(e);
    }
  }
}
