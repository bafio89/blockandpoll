package com.blockandpoll.rest.domain.repository;

import com.algorand.algosdk.crypto.Address;
import com.blockandpoll.rest.domain.model.ApplicationInfoFromBlockchain;
import com.blockandpoll.rest.domain.model.BlockchainPoll;
import com.blockandpoll.rest.domain.request.OptinAppRequest;

public interface BlockchainReadRepository {

  Boolean isAccountSubscribedTo(OptinAppRequest optinAppRequest);

  Boolean hasAddressAlreadyVotedFor(long appId, Address address);

  ApplicationInfoFromBlockchain findApplicationInfoBy(BlockchainPoll poll);
}