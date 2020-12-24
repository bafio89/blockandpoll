package com.pollalgorand.rest.web.endpoint;

import com.pollalgorand.rest.domain.OptinAppRequest;
import com.pollalgorand.rest.web.request.OptinRequest;

public class OptinRequestConverter {

  public OptinAppRequest fromRequestToDomain(long appId, OptinRequest request){

    return new OptinAppRequest(appId, request.getSender(), request.getMnemonicKey());
  }
}
