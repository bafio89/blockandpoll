package com.pollalgorand.rest.domain.repository;

import com.algorand.algosdk.crypto.Address;
import com.pollalgorand.rest.domain.OptinAppRequest;
import java.util.List;

public interface BlockchainReadRepository {

  List<Address> findAddressSubscribedToApplicationBy(String appId);

  Boolean isAccountSubscribedTo(OptinAppRequest optinAppRequest);
}