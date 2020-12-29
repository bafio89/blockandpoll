package com.pollalgorand.rest.adapter.service;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import com.algorand.algosdk.account.Account;
import com.algorand.algosdk.transaction.Transaction;
import com.algorand.algosdk.v2.client.model.TransactionParametersResponse;
import com.pollalgorand.rest.domain.OptinAppRequest;
import java.security.GeneralSecurityException;
import org.jmock.Expectations;
import org.jmock.auto.Mock;
import org.jmock.integration.junit4.JUnitRuleMockery;
import org.jmock.lib.legacy.ClassImposteriser;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

public class BuildOptinTransactionServiceTest {

  public static final String A_MNEMONIC_KEY = "A_MNEMONIC_KEY";
  public static final long APP_ID = 123L;
  public static final String MNEMONIC_KEY = "share gentle refuse logic shield drift earth initial must match aware they perfect chair say jar harvest echo symbol cave ring void prepare above adult";
  @Rule
  public JUnitRuleMockery context = new JUnitRuleMockery() {{
    setImposteriser(ClassImposteriser.INSTANCE);
  }};

  @Mock
  public BlockchainParameterService blockchainParameterService;

  private BuildOptinTransactionService buildOptinTransactionService;

  @Before
  public void setUp() {
    buildOptinTransactionService = new BuildOptinTransactionService(blockchainParameterService);
  }

  @Test
  public void happyPath() throws GeneralSecurityException {

    Transaction expectedOptinTransaction = aTransactionWith();

    context.checking(new Expectations(){{
      oneOf(blockchainParameterService).getParameters();
      will(returnValue(aTransactionParametersResponse()));
    }});

    Transaction transaction = buildOptinTransactionService
        .buildTransaction(new Account(MNEMONIC_KEY), new OptinAppRequest(APP_ID,
            new Account()));

    assertThat(transaction, is(expectedOptinTransaction));
  }

  private Transaction aTransactionWith() throws GeneralSecurityException {

    Account account = new Account(MNEMONIC_KEY);

    return Transaction.ApplicationOptInTransactionBuilder()
        .suggestedParams(aTransactionParametersResponse())
        .sender(account.getAddress())
        .applicationId(APP_ID)
        .build();
  }

  private TransactionParametersResponse aTransactionParametersResponse() {
    TransactionParametersResponse transactionParametersResponse = new TransactionParametersResponse();
    transactionParametersResponse.consensusVersion = "";
    transactionParametersResponse.fee = 1L;
    transactionParametersResponse.genesisHash = new byte[32];
    transactionParametersResponse.genesisId = "";
    transactionParametersResponse.lastRound = 1L;
    return transactionParametersResponse;
  }
}