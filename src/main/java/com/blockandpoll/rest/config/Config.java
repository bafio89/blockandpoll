package com.blockandpoll.rest.config;

import com.algorand.algosdk.v2.client.common.AlgodClient;
import com.algorand.algosdk.v2.client.common.IndexerClient;
import com.blockandpoll.rest.adapter.Clock;
import com.blockandpoll.rest.adapter.TealProgramFactory;
import com.blockandpoll.rest.adapter.converter.AlgorandDateAdapter;
import com.blockandpoll.rest.adapter.converter.PollBlockchainAdapter;
import com.blockandpoll.rest.adapter.repository.AlgorandASCPollRepository;
import com.blockandpoll.rest.adapter.repository.AlgorandReadRepository;
import com.blockandpoll.rest.adapter.repository.AlgorandWriteRepository;
import com.blockandpoll.rest.adapter.service.AccountCreatorService;
import com.blockandpoll.rest.adapter.service.AlgorandApplicationService;
import com.blockandpoll.rest.adapter.service.BlockchainParameterService;
import com.blockandpoll.rest.adapter.service.BuildApplicationCreateTransactionService;
import com.blockandpoll.rest.adapter.service.BuildOptinTransactionService;
import com.blockandpoll.rest.adapter.service.BuildVoteTransactionService;
import com.blockandpoll.rest.adapter.service.TealTextGenerator;
import com.blockandpoll.rest.adapter.service.TransactionConfirmationService;
import com.blockandpoll.rest.adapter.service.TransactionSenderService;
import com.blockandpoll.rest.adapter.service.TransactionSignerService;
import com.blockandpoll.rest.adapter.service.TransactionWriterService;
import com.blockandpoll.rest.adapter.service.UnsignedASCTransactionService;
import com.blockandpoll.rest.domain.DateValidator;
import com.blockandpoll.rest.domain.repository.BlockchainPollRepository;
import com.blockandpoll.rest.domain.repository.BlockchainReadRepository;
import com.blockandpoll.rest.domain.repository.BlockchainWriteRepository;
import com.blockandpoll.rest.domain.repository.PollRepository;
import com.blockandpoll.rest.domain.usecase.CreatePollUseCase;
import com.blockandpoll.rest.domain.usecase.OptinUseCase;
import com.blockandpoll.rest.domain.usecase.RetrievePollUseCase;
import com.blockandpoll.rest.domain.usecase.VoteUseCase;
import com.blockandpoll.rest.web.adapter.OptinRequestConverter;
import com.blockandpoll.rest.web.adapter.PollRequestAdapter;
import com.blockandpoll.rest.web.adapter.VoteRequestConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Config {

  @Bean
  public PollRequestAdapter pollRequestAdapter() {
    return new PollRequestAdapter();
  }

  @Bean
  public PollBlockchainAdapter pollBlockchainAdapter() {
    return new PollBlockchainAdapter(new AlgorandDateAdapter(new Clock()));
  }

  @Bean
  public BlockchainParameterService blockchainParameterService(AlgodClient algodClient) {
    return new BlockchainParameterService(algodClient);
  }

  @Bean
  public TransactionSignerService transactionSignerService() {
    return new TransactionSignerService();
  }

  @Bean
  public TransactionSenderService transactionSenderService(AlgodClient algodClient) {
    return new TransactionSenderService(algodClient);
  }

  @Bean
  public TransactionConfirmationService transactionConfirmationService(AlgodClient algodClient) {
    return new TransactionConfirmationService(algodClient);
  }

  @Bean
  public AccountCreatorService accountCreatorService() {
    return new AccountCreatorService();
  }

  @Bean
  public BuildOptinTransactionService buildOptinTransactionService(
      BlockchainParameterService blockchainParameterService) {
    return new BuildOptinTransactionService(blockchainParameterService);
  }

  @Bean
  public BuildVoteTransactionService buildVoteTransactionService(
      BlockchainParameterService blockchainParameterService) {
    return new BuildVoteTransactionService(blockchainParameterService);
  }

  @Bean
  public AlgorandWriteRepository algorandWriteRepository(
      BuildOptinTransactionService buildOptinTransactionService,
      BuildVoteTransactionService buildVoteTransactionService,
      TransactionWriterService transactionWriterService) {
    return new AlgorandWriteRepository(buildOptinTransactionService,
        buildVoteTransactionService,
        transactionWriterService);
  }

  @Bean
  public UnsignedASCTransactionService unsignedASCTransactionService(AlgodClient algodClient,
      PollBlockchainAdapter pollBlockchainAdapter,
      BlockchainParameterService blockchainParameterService) {
    return new UnsignedASCTransactionService(
        algodClient,
        pollBlockchainAdapter,
        new TealProgramFactory(algodClient, new TealTextGenerator()),
        new BuildApplicationCreateTransactionService(blockchainParameterService));
  }

  @Bean
  public CreatePollUseCase createPollUseCase(BlockchainPollRepository blockchainPollRepository,
      PollRepository postgresPollRepository,
      UnsignedASCTransactionService unsignedASCTransactionService,
      AccountCreatorService accountCreatorService) {
    return new CreatePollUseCase(blockchainPollRepository, postgresPollRepository,
        unsignedASCTransactionService, accountCreatorService);
  }

  @Bean
  public VoteUseCase voteUseCase(PollRepository pollRepository, DateValidator dateValidator,
      BlockchainReadRepository algorandReadRepository,
      BlockchainWriteRepository algorandWriteRepository) {
    return new VoteUseCase(pollRepository, dateValidator, algorandReadRepository,
        algorandWriteRepository);
  }

  @Bean
  public RetrievePollUseCase retrievePollUseCase(PollRepository postgresPollRepository,
      BlockchainReadRepository blockchainReadRepository) {
    return new RetrievePollUseCase(postgresPollRepository, blockchainReadRepository);
  }

  @Bean
  public OptinRequestConverter optinRequestConverter(AccountCreatorService accountCreatorService) {
    return new OptinRequestConverter(accountCreatorService);
  }

  @Bean
  public VoteRequestConverter voteRequestConverter(AccountCreatorService accountCreatorService) {
    return new VoteRequestConverter(accountCreatorService);
  }

  @Bean
  public DateValidator dateValidator() {
    return new DateValidator(new Clock());
  }

  @Bean
  public OptinUseCase optinUseCase(BlockchainReadRepository blockchainReadRepository,
      BlockchainWriteRepository blockchainWriteRepository,
      PollRepository pollRepository, DateValidator dateValidator) {
    return new OptinUseCase(blockchainReadRepository, blockchainWriteRepository, pollRepository,
        dateValidator);
  }

  @Bean
  public AlgodClient algodClient() {

    return new AlgodClient("https://testnet-algorand.api.purestake.io/ps2", 443, "");
//    AlgodClient algodClient = new AlgodClient("https://localhost", 8080, "5a645468dffe417d4ea0682b4ded3a58d2984dcef199a6bb7a70316ba42ac9f5");
  }

  @Bean
  public IndexerClient indexerClient() {
    return new IndexerClient("https://testnet-algorand.api.purestake.io/idx2", 443, "");
  }

  @Bean
  public BlockchainReadRepository algorandReadRepository(IndexerClient indexerClient) {
    return new AlgorandReadRepository(indexerClient);
  }

  @Bean
  public BlockchainPollRepository pollRepository(AlgodClient algodClient,
      AccountCreatorService accountCreatorService,
      UnsignedASCTransactionService unsignedASCTransactionService,
      PollBlockchainAdapter pollBlockchainAdapter,
      TransactionWriterService transactionWriterService) {

    return new AlgorandASCPollRepository(algodClient,
        accountCreatorService,
        unsignedASCTransactionService,
        pollBlockchainAdapter,
        new AlgorandApplicationService(algodClient), transactionWriterService);
  }

  @Bean
  public TransactionWriterService transactionWriterService(
      TransactionSignerService transactionSignerService,
      TransactionSenderService transactionSenderService,
      TransactionConfirmationService transactionConfirmationService) {
    return new TransactionWriterService(transactionSignerService, transactionSenderService,
        transactionConfirmationService);
  }

}
