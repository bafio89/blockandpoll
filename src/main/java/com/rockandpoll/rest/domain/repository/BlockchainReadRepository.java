package com.rockandpoll.rest.domain.repository;

import com.algorand.algosdk.crypto.Address;
import com.rockandpoll.rest.domain.model.ApplicationInfoFromBlockchain;
import com.rockandpoll.rest.domain.model.BlockchainPoll;
import com.rockandpoll.rest.domain.request.OptinAppRequest;

public interface BlockchainReadRepository {

  Boolean isAccountSubscribedTo(OptinAppRequest optinAppRequest);

  Boolean hasAddressAlreadyVotedFor(long appId, Address address);

  ApplicationInfoFromBlockchain findApplicationInfoBy(BlockchainPoll poll);
}