package com.pollalgorand.rest.adapter.repository;

import static com.pollalgorand.rest.adapter.AlgorandUtils.headers;
import static com.pollalgorand.rest.adapter.AlgorandUtils.values;
import static org.junit.rules.ExpectedException.none;

import com.algorand.algosdk.account.Account;
import com.algorand.algosdk.v2.client.common.IndexerClient;
import com.algorand.algosdk.v2.client.common.Response;
import com.algorand.algosdk.v2.client.indexer.LookupAccountByID;
import com.algorand.algosdk.v2.client.model.AccountResponse;
import com.pollalgorand.rest.adapter.exceptions.AlgorandInteractionError;
import com.pollalgorand.rest.domain.OptinAppRequest;
import org.jmock.Expectations;
import org.jmock.auto.Mock;
import org.jmock.integration.junit4.JUnitRuleMockery;
import org.jmock.lib.legacy.ClassImposteriser;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class AlgorandReadRepositoryTest {

  public static final String MNEMONIC_KEY = "share gentle refuse logic shield drift earth initial must match aware they perfect chair say jar harvest echo symbol cave ring void prepare above adult";
  public static final String AN_ERROR_MESSAGE = "AN ERROR MESSAGE";
  @Rule
  public JUnitRuleMockery context = new JUnitRuleMockery(){{
    setImposteriser(ClassImposteriser.INSTANCE);
  }};

  @Rule
  public final ExpectedException expectedException = none();

  @Mock
  private IndexerClient indexerClient;

  @Mock
  private LookupAccountByID lookupAccountByID;

  private AlgorandReadRepository algorandReadRepository;

  @Before
  public void setUp() {
    algorandReadRepository = new AlgorandReadRepository(indexerClient);
  }

  @Test
  public void whenClientGoesInError() throws Exception {

    Account account = new Account(MNEMONIC_KEY);

    context.checking(new Expectations(){{
      oneOf(indexerClient).lookupAccountByID(account.getAddress());
      will(throwException(new RuntimeException(AN_ERROR_MESSAGE)));
    }});

    expectedException.expect(AlgorandInteractionError.class);
    expectedException.expectMessage("An error occurs calling algorand blockchain. AN ERROR MESSAGE");

    algorandReadRepository.isAccountSubscribedTo(new OptinAppRequest(123L, account));
  }

  @Test
  public void whenThereIsAnErrorOnBlockchainInIsAccountSubscribedTo() throws Exception {

    Account account = new Account(MNEMONIC_KEY);
    Response<AccountResponse> response = new Response<>(400, AN_ERROR_MESSAGE, "", "".getBytes());

    context.checking(new Expectations(){{
      oneOf(indexerClient).lookupAccountByID(account.getAddress());
      will(returnValue(lookupAccountByID));

      oneOf(lookupAccountByID).execute(headers, values);
      will(returnValue(response));
    }});

    expectedException.expect(AlgorandInteractionError.class);
    expectedException.expectMessage("An error occurs calling algorand blockchain. Response has code 400. Error message: AN ERROR MESSAGE");

    algorandReadRepository.isAccountSubscribedTo(new OptinAppRequest(123L, account));
  }
}