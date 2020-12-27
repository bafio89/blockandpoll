package com.pollalgorand.rest.config;

import com.algorand.algosdk.v2.client.common.AlgodClient;
import com.pollalgorand.rest.adapter.Clock;
import com.pollalgorand.rest.adapter.TealProgramFactory;
import com.pollalgorand.rest.adapter.converter.AlgorandDateAdapter;
import com.pollalgorand.rest.adapter.converter.PollBlockchainAdapter;
import com.pollalgorand.rest.adapter.repository.AlgorandASCPollRepository;
import com.pollalgorand.rest.adapter.service.AccountCreatorService;
import com.pollalgorand.rest.adapter.service.AlgorandApplicationService;
import com.pollalgorand.rest.adapter.service.BuildApplicationCreateTransactionService;
import com.pollalgorand.rest.adapter.service.TransactionConfirmationService;
import com.pollalgorand.rest.adapter.service.TransactionSignerService;
import com.pollalgorand.rest.adapter.service.UnsignedASCTransactionService;
import com.pollalgorand.rest.domain.DateValidator;
import com.pollalgorand.rest.domain.repository.BlockchainPollRepository;
import com.pollalgorand.rest.domain.repository.PollRepository;
import com.pollalgorand.rest.domain.usecase.CreatePollUseCase;
import com.pollalgorand.rest.domain.usecase.OptinUseCase;
import com.pollalgorand.rest.domain.usecase.RetrievePollUseCase;
import com.pollalgorand.rest.web.adapter.PollRequestAdapter;
import com.pollalgorand.rest.web.endpoint.OptinRequestConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Config {

  @Bean
  public PollRequestAdapter pollRequestAdapter() {
    return new PollRequestAdapter();
  }

  @Bean
  public PollBlockchainAdapter pollBlockchainAdapter(){
    return new PollBlockchainAdapter(new AlgorandDateAdapter(new Clock()));
  }

  @Bean
  public UnsignedASCTransactionService unsignedASCTransactionService(AlgodClient algodClient, PollBlockchainAdapter pollBlockchainAdapter){
    return new UnsignedASCTransactionService(
        algodClient,
        pollBlockchainAdapter,
        new TealProgramFactory(algodClient),
        new BuildApplicationCreateTransactionService(algodClient));
  }

  @Bean
  public CreatePollUseCase createPollUseCase(BlockchainPollRepository blockchainPollRepository,
      PollRepository postgresPollRepository, UnsignedASCTransactionService unsignedASCTransactionService) {
    return new CreatePollUseCase(blockchainPollRepository, postgresPollRepository,
        unsignedASCTransactionService);
  }

  @Bean
  public RetrievePollUseCase retrievePollUseCase(PollRepository postgresPollRepository) {
    return new RetrievePollUseCase(postgresPollRepository);
  }

  @Bean
  public OptinRequestConverter optinRequestConverter(){
    return new OptinRequestConverter();
  }

  @Bean
  public OptinUseCase optinUseCase(PollRepository pollRepository){
    return new OptinUseCase(null, null, pollRepository, new DateValidator());
  }

  @Bean
  public AlgodClient algodClient(){

    return new AlgodClient("https://testnet-algorand.api.purestake.io/ps2", 443, "");
//    AlgodClient algodClient = new AlgodClient("https://localhost", 8080, "5a645468dffe417d4ea0682b4ded3a58d2984dcef199a6bb7a70316ba42ac9f5");

  }

  @Bean
  public BlockchainPollRepository pollRepository(AlgodClient algodClient, UnsignedASCTransactionService unsignedASCTransactionService, PollBlockchainAdapter pollBlockchainAdapter) {

    return new AlgorandASCPollRepository(algodClient,
        new AccountCreatorService(), new TransactionSignerService(),
        unsignedASCTransactionService,
        pollBlockchainAdapter,
        new TransactionConfirmationService(algodClient),
        new AlgorandApplicationService(algodClient));
  }

}
