package com.pollalgorand.rest.domain.repository;

import com.pollalgorand.rest.domain.OptinAppRequest;

public interface BlockchainWriteRepository {

  void optin(OptinAppRequest optinAppRequest);
}
