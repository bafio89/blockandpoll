package com.pollalgorand.rest.adapter.repository;

import com.algorand.algosdk.v2.client.common.AlgodClient;
import com.pollalgorand.rest.adapter.service.AccountCreatorService;
import com.pollalgorand.rest.adapter.service.BuildOptinTransactionService;
import com.pollalgorand.rest.adapter.service.TransactionConfirmationService;
import com.pollalgorand.rest.adapter.service.TransactionSignerService;
import com.pollalgorand.rest.domain.OptinAppRequest;
import com.pollalgorand.rest.domain.repository.BlockchainWriteRepository;
import org.jmock.auto.Mock;
import org.jmock.integration.junit4.JUnitRuleMockery;
import org.jmock.lib.legacy.ClassImposteriser;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

public class AlgorandWriteRepositoryTest {

  @Rule
  public JUnitRuleMockery context = new JUnitRuleMockery(){{
    setImposteriser(ClassImposteriser.INSTANCE);
  }};

  @Mock
  public AlgodClient algodClient;

  @Mock
  public BuildOptinTransactionService buildOptinTransactionService;

  @Mock
  public AccountCreatorService accountCreator;

  @Mock
  public TransactionSignerService transactionSignerService;

  @Mock
  public TransactionConfirmationService transactionConfirmationService;


  private BlockchainWriteRepository blockchainWriteRepository;

  @Before
  public void setUp() {
    blockchainWriteRepository = new AlgorandWriteRepository(algodClient,
        buildOptinTransactionService, transactionSignerService, transactionConfirmationService);
  }

  @Test
  public void happyPath() {

    blockchainWriteRepository.optin(new OptinAppRequest(123L, "A_MNEMONIC_KEY"));
  }
}