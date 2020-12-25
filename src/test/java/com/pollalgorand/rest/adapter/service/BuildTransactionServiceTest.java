package com.pollalgorand.rest.adapter.service;

import static com.pollalgorand.rest.ByteConverteUtil.convertLongToByteArray;
import static java.nio.charset.StandardCharsets.UTF_8;
import static java.util.Arrays.asList;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.rules.ExpectedException.none;

import com.algorand.algosdk.crypto.TEALProgram;
import com.algorand.algosdk.logic.StateSchema;
import com.algorand.algosdk.transaction.Transaction;
import com.algorand.algosdk.v2.client.algod.TransactionParams;
import com.algorand.algosdk.v2.client.common.AlgodClient;
import com.algorand.algosdk.v2.client.common.Response;
import com.algorand.algosdk.v2.client.model.TransactionParametersResponse;
import com.pollalgorand.rest.adapter.PollTealParams;
import com.pollalgorand.rest.adapter.exceptions.InvalidSenderAddressException;
import java.util.List;
import org.jmock.Expectations;
import org.jmock.auto.Mock;
import org.jmock.integration.junit4.JUnitRuleMockery;
import org.jmock.lib.legacy.ClassImposteriser;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class BuildTransactionServiceTest {

  public static final String A_POLL_NAME = "A_POLL_NAME";
  public static final String SENDER_ADDRESS = "GM5YGY4ICDLE27NCVFR6OS7JIIXSGYI6SQIF5IPKQTTGO2YIJU5YOZDP2A";
  public static byte[] A_START_SUBS_BLOCK_NUMBER = convertLongToByteArray(1L);
  public static byte[] A_END_SUBS_BLOCK_NUMBER = convertLongToByteArray(2L);
  public static byte[] A_START_VOTE_BLOCK_NUMBER = convertLongToByteArray(3L);
  public static byte[] A_END_VOTE_BLOCK_NUMBER = convertLongToByteArray(4L);
  private static final List<String> OPTIONS_IN_BYTE = asList("OPTION_1", "OPTION_2");

  @Rule
  public JUnitRuleMockery context = new JUnitRuleMockery() {{
    setImposteriser(ClassImposteriser.INSTANCE);
  }};

  @Rule
  public final ExpectedException expectedException = none();

  @Mock
  private AlgodClient algodClient;

  @Mock
  private TransactionParams transactionParams;

  @Mock
  private Response response;

  private final String[] headers = {"X-API-Key"};
  private final String[] values = {"KmeYVcOTUFayYL9uVy9mI9d7dDewlWth7pprTlo9"};

  private BuildTransactionService buildTransactionService;

  private TEALProgram approvalProgram = new TEALProgram();
  private TEALProgram clearStateProgram = new TEALProgram();

  private PollTealParams pollTealParams;

  @Before
  public void setUp() {

    buildTransactionService = new BuildTransactionService(algodClient);

    pollTealParams = new PollTealParams(A_POLL_NAME.getBytes(UTF_8),
        A_START_SUBS_BLOCK_NUMBER, A_END_SUBS_BLOCK_NUMBER, A_START_VOTE_BLOCK_NUMBER,
        A_END_VOTE_BLOCK_NUMBER, OPTIONS_IN_BYTE, SENDER_ADDRESS.getBytes(UTF_8));
  }

  @Test
  public void buildTransaction() throws Exception {

    TransactionParametersResponse transactionParametersResponse = aTransactionParametersResponse();

    Transaction expectedTransaction = aTransactionWith(approvalProgram, clearStateProgram,
        transactionParametersResponse);

    context.checking(new Expectations() {{
      oneOf(algodClient).TransactionParams();
      will(returnValue(transactionParams));

      oneOf(transactionParams).execute(headers, values);
      will(returnValue(response));

      oneOf(response).body();
      will(returnValue(transactionParametersResponse));
    }});

    Transaction transaction = buildTransactionService
        .buildTransaction(pollTealParams, approvalProgram, clearStateProgram, SENDER_ADDRESS);

    assertThat(transaction, is(expectedTransaction));
  }

  @Test
  public void whenSenderAddressIsNotValid() throws Exception {

    TransactionParametersResponse transactionParametersResponse = aTransactionParametersResponse();

    context.checking(new Expectations() {{
      oneOf(algodClient).TransactionParams();
      will(returnValue(transactionParams));

      oneOf(transactionParams).execute(headers, values);
      will(returnValue(response));

      oneOf(response).body();
      will(returnValue(transactionParametersResponse));
    }});

    expectedException.expect(InvalidSenderAddressException.class);

    buildTransactionService
        .buildTransaction(pollTealParams, approvalProgram, clearStateProgram, "INVALID_SENDER_ADDRESS");

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

  private Transaction aTransactionWith(TEALProgram approvalProgram, TEALProgram clearStateProgram,
      TransactionParametersResponse transactionParametersResponse) {
    return Transaction.ApplicationCreateTransactionBuilder()
        .sender(SENDER_ADDRESS)
        .args(asList(A_START_SUBS_BLOCK_NUMBER,
            A_END_SUBS_BLOCK_NUMBER,
            A_START_VOTE_BLOCK_NUMBER,
            A_END_VOTE_BLOCK_NUMBER))
        .suggestedParams(transactionParametersResponse)
        .approvalProgram(approvalProgram)
        .clearStateProgram(clearStateProgram)
        .globalStateSchema(new StateSchema())
        .localStateSchema(new StateSchema())
        .build();
  }
}