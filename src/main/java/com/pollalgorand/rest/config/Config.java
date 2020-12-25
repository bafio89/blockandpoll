package com.pollalgorand.rest.config;

import com.algorand.algosdk.v2.client.common.AlgodClient;
import com.pollalgorand.rest.adapter.Clock;
import com.pollalgorand.rest.adapter.TealProgramFactory;
import com.pollalgorand.rest.adapter.converter.AlgorandDateAdapter;
import com.pollalgorand.rest.adapter.converter.PollBlockchainParamsAdapter;
import com.pollalgorand.rest.adapter.repository.AlgorandASCPollRepository;
import com.pollalgorand.rest.adapter.service.BuildTransactionService;
import com.pollalgorand.rest.domain.repository.BlockchainPollRepository;
import com.pollalgorand.rest.domain.repository.PollRepository;
import com.pollalgorand.rest.domain.usecase.CreatePollUseCase;
import com.pollalgorand.rest.domain.usecase.RetrievePollUseCase;
import com.pollalgorand.rest.web.adapter.PollRequestAdapter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Config {

  @Bean
  public PollRequestAdapter pollRequestAdapter(){
    return new PollRequestAdapter();
  }

  @Bean
  public CreatePollUseCase createPollUseCase(BlockchainPollRepository blockchainPollRepository, PollRepository postgresPollRepository){
    return new CreatePollUseCase(blockchainPollRepository, postgresPollRepository);
  }

  @Bean
  public RetrievePollUseCase retrievePollUseCase(PollRepository postgresPollRepository){
    return new RetrievePollUseCase(postgresPollRepository);
  }

  @Bean
  public BlockchainPollRepository pollRepository(){

    AlgodClient algodClient = new AlgodClient("https://testnet-algorand.api.purestake.io/ps2", 443, "");
//    AlgodClient algodClient = new AlgodClient("https://localhost", 8080, "5a645468dffe417d4ea0682b4ded3a58d2984dcef199a6bb7a70316ba42ac9f5");

    return new AlgorandASCPollRepository(algodClient, new TealProgramFactory(algodClient), new PollBlockchainParamsAdapter(new AlgorandDateAdapter(new Clock())),
        new BuildTransactionService(algodClient));
  }

}
