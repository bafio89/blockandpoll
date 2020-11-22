package com.pollalgorand.rest;

import static java.nio.charset.StandardCharsets.UTF_8;
import static java.util.Arrays.asList;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.rules.ExpectedException.none;

import com.algorand.algosdk.crypto.TEALProgram;
import com.algorand.algosdk.logic.StateSchema;
import com.algorand.algosdk.transaction.Transaction;
import com.algorand.algosdk.v2.client.algod.GetStatus;
import com.algorand.algosdk.v2.client.algod.TransactionParams;
import com.algorand.algosdk.v2.client.common.AlgodClient;
import com.algorand.algosdk.v2.client.common.Response;
import com.algorand.algosdk.v2.client.model.NodeStatusResponse;
import com.algorand.algosdk.v2.client.model.TransactionParametersResponse;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import org.jmock.Expectations;
import org.jmock.auto.Mock;
import org.jmock.integration.junit4.JUnitRuleMockery;
import org.jmock.lib.legacy.ClassImposteriser;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class AlgorandPollRepositoryTest {

  public static final String SENDER_ADDRESS = "GM5YGY4ICDLE27NCVFR6OS7JIIXSGYI6SQIF5IPKQTTGO2YIJU5YOZDP2A";
  public static final String INVALID_SENDER_ADDRESS = "INVALID SENDER ADDRESS";
  public static final String WRONG_SENDER_ADDRESS_ERROR_MESSAGE = "Something went wrong with sender address during transaction creation: Last encoded character (before the paddings if any) is a valid base 32 alphabet but not a possible value. Expected the discarded bits to be zero.";
  public static final long LAST_ROUND = 1L;
  public static final long A_BLOCK_NUMBER = 1L;
  @Rule
  public JUnitRuleMockery context = new JUnitRuleMockery(){{
    setImposteriser(ClassImposteriser.INSTANCE);
  }};

  @Rule public final ExpectedException expectedException = none();

  @Mock
  private AlgodClient algodClient;

  @Mock
  private TealProgramFactory tealProgramFactory;
  @Mock
  private PollBlockchainParamsAdapter pollBlockchainParamsAdapter;
  @Mock
  private TransactionParams transactionParams;
  @Mock
  private Response response;
  @Mock
  private Response statusResponse;
  @Mock
  private GetStatus getStatus;

  private AlgorandPollRepository algorandPollRepository;
  private static final List<String> OPTIONS_IN_BYTE = asList("OPTION_1", "OPTION_2");
  private NodeStatusResponse nodeStatusResponse = new NodeStatusResponse();


  @Before
  public void setUp() {

    nodeStatusResponse.lastRound = LAST_ROUND;

    algorandPollRepository = new AlgorandPollRepository(algodClient,
        tealProgramFactory, pollBlockchainParamsAdapter);
  }

  @Test
  public void createAPollUnsignedTransaction() throws Exception {
    TEALProgram approvalProgram = new TEALProgram();
    TEALProgram clearStateProgram = new TEALProgram();

    TransactionParametersResponse transactionParametersResponse = aTransactionParametersResponse();

    Transaction expectedTransaction = aTransactionWith(approvalProgram, clearStateProgram,
        transactionParametersResponse);

    Poll poll = aPollWith(SENDER_ADDRESS);

    PollTealParams pollTealParams = new PollTealParams(poll.getName().getBytes(UTF_8),
        A_BLOCK_NUMBER, A_BLOCK_NUMBER, A_BLOCK_NUMBER, A_BLOCK_NUMBER, OPTIONS_IN_BYTE, SENDER_ADDRESS.getBytes());

    context.checking(new Expectations(){{

      oneOf(algodClient).GetStatus();
      will(returnValue(getStatus));

      oneOf(getStatus).execute();
      will(returnValue(statusResponse));

      oneOf(statusResponse).body();
      will(returnValue(nodeStatusResponse));

      oneOf(pollBlockchainParamsAdapter).fromPollToPollTealParams(poll, LAST_ROUND);
      will(returnValue(pollTealParams));

      oneOf(tealProgramFactory).createApprovalProgramFrom(pollTealParams);
      will(returnValue(approvalProgram));

      oneOf(tealProgramFactory).createClearStateProgram();
      will(returnValue(clearStateProgram));

      oneOf(algodClient).TransactionParams();
      will(returnValue(transactionParams));

      oneOf(transactionParams).execute();
      will(returnValue(response));

      oneOf(response).body();
      will(returnValue(transactionParametersResponse));
    }});

    Transaction unsignedTx = algorandPollRepository.createUnsignedTx(poll);

    assertThat(unsignedTx, is(expectedTransaction));

  }

  private Poll aPollWith(String sender) {
    return new Poll("A_POLL", LocalDateTime.now(), LocalDateTime.now(), LocalDateTime.now(), LocalDateTime.now(), asList("OPTION_1", "OPTION_2"),
        sender);
  }

  @Test
  public void whenSenderAddressIsNotValid() throws Exception {
    TEALProgram approvalProgram = new TEALProgram();
    TEALProgram clearStateProgram = new TEALProgram();
    Poll poll = aPollWith(INVALID_SENDER_ADDRESS);

    PollTealParams pollTealParams = new PollTealParams(poll.getName().getBytes(UTF_8), A_BLOCK_NUMBER,
        A_BLOCK_NUMBER, A_BLOCK_NUMBER, A_BLOCK_NUMBER, OPTIONS_IN_BYTE,
        INVALID_SENDER_ADDRESS.getBytes());

    context.checking(new Expectations(){{

      oneOf(algodClient).GetStatus();
      will(returnValue(getStatus));

      oneOf(getStatus).execute();
      will(returnValue(statusResponse));

      oneOf(statusResponse).body();
      will(returnValue(nodeStatusResponse));

      oneOf(pollBlockchainParamsAdapter).fromPollToPollTealParams(poll, LAST_ROUND);
      will(returnValue(pollTealParams));

      oneOf(tealProgramFactory).createApprovalProgramFrom(pollTealParams);
      will(returnValue(approvalProgram));

      oneOf(tealProgramFactory).createClearStateProgram();
      will(returnValue(clearStateProgram));
    }});

    expectedException.expect(InvalidSenderAddressException.class);
    expectedException.expectMessage(
        WRONG_SENDER_ADDRESS_ERROR_MESSAGE);

    algorandPollRepository.createUnsignedTx(poll);

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
        .suggestedParams(transactionParametersResponse)
        .approvalProgram(approvalProgram)
        .clearStateProgram(clearStateProgram)
        .globalStateSchema(new StateSchema())
        .localStateSchema(new StateSchema())
        .build();
  }

}