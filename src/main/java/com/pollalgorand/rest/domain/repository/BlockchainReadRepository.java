package com.pollalgorand.rest.domain.repository;

import com.algorand.algosdk.crypto.Address;
import com.pollalgorand.rest.domain.ApplicationInfoFromBlockchain;
import com.pollalgorand.rest.domain.model.BlockchainPoll;
import com.pollalgorand.rest.domain.request.OptinAppRequest;

public interface BlockchainReadRepository {

  Boolean isAccountSubscribedTo(OptinAppRequest optinAppRequest);

  Boolean hasAddressAlreadyVotedFor(long appId, Address address);

  ApplicationInfoFromBlockchain findApplicationInfoBy(BlockchainPoll poll);
}