package com.pollalgorand.rest;

import static com.pollalgorand.rest.ByteConverteUtil.convertLongToByteArray;
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
import com.pollalgorand.rest.adapter.PollTealParams;
import com.pollalgorand.rest.adapter.TealProgramFactory;
import com.pollalgorand.rest.adapter.converter.PollBlockchainParamsAdapter;
import com.pollalgorand.rest.adapter.exceptions.InvalidSenderAddressException;
import com.pollalgorand.rest.adapter.exceptions.NodeStatusException;
import com.pollalgorand.rest.adapter.repository.AlgorandASCPollRepository;
import com.pollalgorand.rest.domain.model.Poll;
import java.time.LocalDateTime;
import java.util.List;
import org.jmock.Expectations;
import org.jmock.auto.Mock;
import org.jmock.integration.junit4.JUnitRuleMockery;
import org.jmock.lib.legacy.ClassImposteriser;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class AlgorandASCPollRepositoryTest {

  public static final String SENDER_ADDRESS = "GM5YGY4ICDLE27NCVFR6OS7JIIXSGYI6SQIF5IPKQTTGO2YIJU5YOZDP2A";
  public static final String INVALID_SENDER_ADDRESS = "INVALID SENDER ADDRESS";
  public static final String WRONG_SENDER_ADDRESS_ERROR_MESSAGE = "Something went wrong with sender address during transaction creation: Last encoded character (before the paddings if any) is a valid base 32 alphabet but not a possible value. Expected the discarded bits to be zero.";
  public static final long LAST_ROUND = 1L;
  public static byte [] A_BLOCK_NUMBER;
  public static byte [] A_START_SUBS_BLOCK_NUMBER;
  public static byte [] A_END_SUBS_BLOCK_NUMBER;
  public static byte [] A_START_VOTE_BLOCK_NUMBER;
  public static byte [] A_END_VOTE_BLOCK_NUMBER;
  public static final String NODE_STATUS_ERROR_MESSAGE = "Something goes wrong getting node status: AN_ERROR";

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

  private AlgorandASCPollRepository algorandASCPollRepository;
  private static final List<String> OPTIONS_IN_BYTE = asList("OPTION_1", "OPTION_2");
  private NodeStatusResponse nodeStatusResponse = new NodeStatusResponse();


  @Before
  public void setUp() {

    nodeStatusResponse.lastRound = LAST_ROUND;

    algorandASCPollRepository = new AlgorandASCPollRepository(algodClient,
        tealProgramFactory, pollBlockchainParamsAdapter);

    A_BLOCK_NUMBER = convertLongToByteArray(1L);
    A_START_SUBS_BLOCK_NUMBER = convertLongToByteArray(1L);
    A_END_SUBS_BLOCK_NUMBER = convertLongToByteArray(2L);
    A_START_VOTE_BLOCK_NUMBER = convertLongToByteArray(3L);
    A_END_VOTE_BLOCK_NUMBER = convertLongToByteArray(4L);
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
        A_START_SUBS_BLOCK_NUMBER, A_END_SUBS_BLOCK_NUMBER, A_START_VOTE_BLOCK_NUMBER,
        A_END_VOTE_BLOCK_NUMBER, OPTIONS_IN_BYTE, SENDER_ADDRESS.getBytes());

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

    Transaction unsignedTx = algorandASCPollRepository.createUnsignedTxFor(poll);

    assertThat(unsignedTx, is(expectedTransaction));
//    assertThat(unsignedTx.applicationArgs.get(0), is(expectedTransaction.applicationArgs.get(0)));
//    assertThat(unsignedTx.applicationArgs.get(1), is(expectedTransaction.applicationArgs.get(1)));
//    assertThat(unsignedTx.applicationArgs.get(2), is(expectedTransaction.applicationArgs.get(2)));
//    assertThat(unsignedTx.applicationArgs.get(3), is(expectedTransaction.applicationArgs.get(3)));
  }

  private Poll aPollWith(String sender) {
    return new Poll("A_POLL", LocalDateTime.now(), LocalDateTime.now(), LocalDateTime.now(), LocalDateTime.now(),
        asList("OPTION_1", "OPTION_2"),
        sender, "mnemonicKey", "description");
  }

  @Test
  public void whenSenderAddressIsNotValid() throws Exception {
    TEALProgram approvalProgram = new TEALProgram();
    TEALProgram clearStateProgram = new TEALProgram();
    Poll poll = aPollWith(INVALID_SENDER_ADDRESS);

    PollTealParams pollTealParams = new PollTealParams(poll.getName().getBytes(UTF_8),
        A_BLOCK_NUMBER, A_BLOCK_NUMBER, A_BLOCK_NUMBER,
        A_BLOCK_NUMBER, OPTIONS_IN_BYTE,
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

    algorandASCPollRepository.createUnsignedTxFor(poll);

  }

  @Test
  public void whenGettingNodeStatusFails() throws Exception {
    Poll poll = aPollWith(INVALID_SENDER_ADDRESS);

    PollTealParams pollTealParams = new PollTealParams(poll.getName().getBytes(UTF_8),
        A_BLOCK_NUMBER, A_BLOCK_NUMBER, A_BLOCK_NUMBER,
        A_BLOCK_NUMBER, OPTIONS_IN_BYTE,
        INVALID_SENDER_ADDRESS.getBytes());

    context.checking(new Expectations(){{

      oneOf(algodClient).GetStatus();
      will(returnValue(getStatus));

      oneOf(getStatus).execute();
      will(throwException(new Exception("AN_ERROR")));

      never(statusResponse).body();

      never(pollBlockchainParamsAdapter).fromPollToPollTealParams(poll, LAST_ROUND);

      never(tealProgramFactory).createApprovalProgramFrom(pollTealParams);

      never(tealProgramFactory).createClearStateProgram();

    }});

    expectedException.expect(NodeStatusException.class);
    expectedException.expectMessage(
        NODE_STATUS_ERROR_MESSAGE);

    algorandASCPollRepository.createUnsignedTxFor(poll);

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