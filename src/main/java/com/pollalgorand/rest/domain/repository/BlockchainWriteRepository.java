package com.pollalgorand.rest.domain.repository;

import com.pollalgorand.rest.domain.request.OptinAppRequest;

public interface BlockchainWriteRepository {

  void optin(OptinAppRequest optinAppRequest);
}
