package com.blockandpoll.rest.adapter.service;

import static java.nio.charset.StandardCharsets.UTF_8;
import static java.util.Arrays.asList;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import com.algorand.algosdk.account.Account;
import com.algorand.algosdk.transaction.Transaction;
import com.algorand.algosdk.v2.client.model.TransactionParametersResponse;
import com.blockandpoll.rest.domain.request.VoteAppRequest;
import java.security.GeneralSecurityException;
import org.jmock.Expectations;
import org.jmock.auto.Mock;
import org.jmock.integration.junit4.JUnitRuleMockery;
import org.jmock.lib.legacy.ClassImposteriser;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

public class BuildVoteTransactionServiceTest {

  public static final long APP_ID = 123L;
  public static final String MNEMONIC_KEY = "share gentle refuse logic shield drift earth initial must match aware they perfect chair say jar harvest echo symbol cave ring void prepare above adult";

  @Rule
  public JUnitRuleMockery context = new JUnitRuleMockery() {{
    setImposteriser(ClassImposteriser.INSTANCE);
  }};

  @Mock
  public BlockchainParameterService blockchainParameterService;

  private BuildVoteTransactionService buildVoteTransactionService;

  private Account account;

  @Before
  public void setUp() throws GeneralSecurityException {

    buildVoteTransactionService = new BuildVoteTransactionService(blockchainParameterService);
    account = new Account(MNEMONIC_KEY);
  }

  @Test
  public void happyPath() {

    Transaction expectedVoteTransaction = aTransactionWith();

    context.checking(new Expectations() {{
      oneOf(blockchainParameterService).getParameters();
      will(returnValue(aTransactionParametersResponse()));
    }});

    Transaction transaction = buildVoteTransactionService
        .buildTransaction(new VoteAppRequest(APP_ID, account, "AN OPTION"));

    assertThat(transaction, is(expectedVoteTransaction));
  }

  private Transaction aTransactionWith() {
    return Transaction.ApplicationCallTransactionBuilder()
        .suggestedParams(aTransactionParametersResponse())
        .sender(account.getAddress())
        .args(asList("str:vote".getBytes(UTF_8),
            String.format("str:%s", "AN OPTION").getBytes(UTF_8)))
        .applicationId(APP_ID).build();
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