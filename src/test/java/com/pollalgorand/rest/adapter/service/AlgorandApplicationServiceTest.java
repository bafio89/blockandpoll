package com.pollalgorand.rest.adapter.service;

import static com.pollalgorand.rest.adapter.AlgorandUtils.headers;
import static com.pollalgorand.rest.adapter.AlgorandUtils.values;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.rules.ExpectedException.none;

import com.algorand.algosdk.v2.client.algod.PendingTransactionInformation;
import com.algorand.algosdk.v2.client.common.AlgodClient;
import com.algorand.algosdk.v2.client.common.Response;
import com.algorand.algosdk.v2.client.model.PendingTransactionResponse;
import com.pollalgorand.rest.adapter.exceptions.RetrievingApplicationIdException;
import org.jmock.Expectations;
import org.jmock.auto.Mock;
import org.jmock.integration.junit4.JUnitRuleMockery;
import org.jmock.lib.legacy.ClassImposteriser;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class AlgorandApplicationServiceTest {

  public static final long APPLICATION_INDEX = 123L;
  @Rule
  public JUnitRuleMockery context = new JUnitRuleMockery() {{
    setImposteriser(ClassImposteriser.INSTANCE);
  }};

  @Rule
  public final ExpectedException expectedException = none();

  @Mock
  private AlgodClient algodClient;

  @Mock
  private PendingTransactionInformation pendingTransactionInformation;

  @Mock
  private Response response;

  private AlgorandApplicationService algorandApplicationService;

  @Before
  public void setUp() {

    algorandApplicationService = new AlgorandApplicationService(algodClient);
  }

  @Test
  public void applicationIdIsRetrieved() throws Exception {

    String transactionId = "999";

    PendingTransactionResponse pendingTransactionResponse = new PendingTransactionResponse();
    pendingTransactionResponse.applicationIndex = APPLICATION_INDEX;

    context.checking(new Expectations() {{
      oneOf(algodClient).PendingTransactionInformation(transactionId);
      will(returnValue(pendingTransactionInformation));

      oneOf(pendingTransactionInformation).execute(headers, values);
      will(returnValue(response));

      oneOf(response).body();
      will(returnValue(pendingTransactionResponse));

    }});

    assertThat(APPLICATION_INDEX, is(algorandApplicationService.getApplicationId(transactionId)));
  }

  @Test
  public void whenThereIsAnError() {
    String transactionId = "999";

    context.checking(new Expectations() {{
      oneOf(algodClient).PendingTransactionInformation(transactionId);
      will(throwException(new RuntimeException("AN ERROR")));
    }});

    expectedException.expect(RetrievingApplicationIdException.class);
    expectedException.expectMessage("Impossible to get Application id for transaction: 999. Error message: AN ERROR");

    algorandApplicationService.getApplicationId(transactionId);
  }
}