package com.blockandpoll.rest.adapter.repository;

import static com.blockandpoll.rest.adapter.AlgorandUtils.headers;
import static com.blockandpoll.rest.adapter.AlgorandUtils.values;
import static java.util.Arrays.asList;
import static org.junit.rules.ExpectedException.none;

import com.algorand.algosdk.account.Account;
import com.algorand.algosdk.v2.client.common.IndexerClient;
import com.algorand.algosdk.v2.client.common.Response;
import com.algorand.algosdk.v2.client.indexer.LookupAccountByID;
import com.algorand.algosdk.v2.client.indexer.LookupApplicationByID;
import com.algorand.algosdk.v2.client.indexer.SearchForAccounts;
import com.algorand.algosdk.v2.client.model.AccountResponse;
import com.algorand.algosdk.v2.client.model.Application;
import com.algorand.algosdk.v2.client.model.ApplicationResponse;
import com.blockandpoll.rest.adapter.exceptions.AlgorandInteractionError;
import com.blockandpoll.rest.domain.model.BlockchainPoll;
import com.blockandpoll.rest.domain.request.OptinAppRequest;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.time.LocalDateTime;
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
  public static final long APP_ID = 123L;
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

  @Mock
  private SearchForAccounts searchForAccounts;

  @Mock
  private LookupApplicationByID lookupApplicationByID;

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

    algorandReadRepository.isAccountSubscribedTo(new OptinAppRequest(APP_ID, account));
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

    algorandReadRepository.isAccountSubscribedTo(new OptinAppRequest(APP_ID, account));
  }

  @Test
  public void whenFindingApplicationInfoFindOptionDoNotReturn200() throws Exception {

    Response<ApplicationResponse> response = new Response<>(400, AN_ERROR_MESSAGE, "", "".getBytes());

    context.checking(new Expectations(){{
      oneOf(indexerClient).lookupApplicationByID(APP_ID);
      will(returnValue(lookupApplicationByID));

      oneOf(lookupApplicationByID).execute(headers,values);
      will(returnValue(response));
    }});

    expectedException.expect(AlgorandInteractionError.class);
    expectedException.expectMessage("An error occurs calling algorand blockchain. Response has code 400. Error message: AN ERROR MESSAGE");

    algorandReadRepository.findApplicationInfoBy(aPoll(APP_ID));
  }

  @Test
  public void whenFindingApplicationInfoFindAddressSubscribedDoNotReturn200() throws Exception {

    context.checking(new Expectations(){{

      oneOf(indexerClient).lookupApplicationByID(APP_ID);
      will(returnValue(lookupApplicationByID));

      oneOf(lookupApplicationByID).execute(headers,values);
      will(throwException(new RuntimeException("AN ERROR")));

    }});

    expectedException.expect(AlgorandInteractionError.class);
    expectedException.expectMessage("An error occurs calling algorand blockchain. AN ERROR");

    algorandReadRepository.findApplicationInfoBy(aPoll(APP_ID));
  }

  private BlockchainPoll aPoll(long appId) {
    return new BlockchainPoll(appId, "", "", LocalDateTime.now(), LocalDateTime.now(),
        LocalDateTime.now(), LocalDateTime.now(), asList("melone", "prosciutto"), "", "");
  }

  public byte [] getBytes(Application application){
    ByteArrayOutputStream bos = new ByteArrayOutputStream();
    ObjectOutputStream oos;
    try {
      oos = new ObjectOutputStream(bos);
      oos.writeObject(application);
      oos.flush();
    } catch (IOException e) {
      e.printStackTrace();
    }
    return bos.toByteArray();
  }
}