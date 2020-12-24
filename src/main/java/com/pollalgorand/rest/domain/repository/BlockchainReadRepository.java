package com.pollalgorand.rest.domain.repository;

import com.pollalgorand.rest.domain.OptinAppRequest;

public interface BlockchainReadRepository {

  Boolean isOptinAllowedFor(OptinAppRequest optinAppRequest);
}
