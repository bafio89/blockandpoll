package com.pollalgorand.rest.web.adapter;

import com.pollalgorand.rest.domain.request.VoteAppRequest;
import com.pollalgorand.rest.web.request.VoteRequest;

public class VoteRequestConverter {


  public VoteAppRequest fromRequestToDomain(long appId, VoteRequest voteRequest) {

    return new VoteAppRequest();
  }
}
