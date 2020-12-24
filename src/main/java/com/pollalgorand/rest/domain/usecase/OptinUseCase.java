package com.pollalgorand.rest.domain.usecase;

import com.pollalgorand.rest.domain.OptinAppRequest;
import com.pollalgorand.rest.domain.exceptions.OptinAlreadDoneException;
import com.pollalgorand.rest.domain.repository.BlockchainReadRepository;
import com.pollalgorand.rest.domain.repository.BlockchainWriteRepository;

public class OptinUseCase {


  private final BlockchainReadRepository blockChainReadRepository;
  private final BlockchainWriteRepository blockchainWriteRepository;

  public OptinUseCase(BlockchainReadRepository blockChainReadRepository,
      BlockchainWriteRepository blockchainWriteRepository) {

    this.blockChainReadRepository = blockChainReadRepository;
    this.blockchainWriteRepository = blockchainWriteRepository;
  }

  public void optin(OptinAppRequest optinAppRequest) {

    if(blockChainReadRepository.isOptinAllowedFor(optinAppRequest)){
      blockchainWriteRepository.optin(optinAppRequest);
    }else{
      throw new OptinAlreadDoneException(optinAppRequest.getAppId(), optinAppRequest.getSender());
    }

  }
}
