package com.pollalgorand.rest.config;

import com.algorand.algosdk.v2.client.common.AlgodClient;
import com.pollalgorand.rest.AlgorandDateAdapter;
import com.pollalgorand.rest.AlgorandPollRepository;
import com.pollalgorand.rest.Clock;
import com.pollalgorand.rest.CreatePollUseCase;
import com.pollalgorand.rest.PollBlockchainParamsAdapter;
import com.pollalgorand.rest.PollRepository;
import com.pollalgorand.rest.PollRequestAdapter;
import com.pollalgorand.rest.TealProgramFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Config {

  @Bean
  public PollRequestAdapter pollRequestAdapter(){
    return new PollRequestAdapter();
  }

  @Bean
  public CreatePollUseCase createPollUseCase(PollRepository pollRepository){
    return new CreatePollUseCase(pollRepository, null);
  }

  @Bean
  public PollRepository pollRepository(){

    AlgodClient algodClient = new AlgodClient("https://testnet-algorand.api.purestake.io/ps2", 443, "");
//    AlgodClient algodClient = new AlgodClient("https://localhost", 8080, "5a645468dffe417d4ea0682b4ded3a58d2984dcef199a6bb7a70316ba42ac9f5");

    return new AlgorandPollRepository(algodClient, new TealProgramFactory(algodClient), new PollBlockchainParamsAdapter(new AlgorandDateAdapter(new Clock())));
  }

}