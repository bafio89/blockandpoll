package com.pollalgorand.rest.adapter.service;

import static com.pollalgorand.rest.ByteConverteUtil.convertLongToByteArray;
import static java.util.Collections.emptyList;
import static org.junit.rules.ExpectedException.none;

import com.algorand.algosdk.crypto.TEALProgram;
import com.algorand.algosdk.v2.client.algod.GetStatus;
import com.algorand.algosdk.v2.client.common.AlgodClient;
import com.algorand.algosdk.v2.client.common.Response;
import com.algorand.algosdk.v2.client.model.NodeStatusResponse;
import com.pollalgorand.rest.adapter.PollTealParams;
import com.pollalgorand.rest.adapter.TealProgramFactory;
import com.pollalgorand.rest.adapter.converter.PollBlockchainAdapter;
import com.pollalgorand.rest.adapter.exceptions.InvalidSenderAddressException;
import com.pollalgorand.rest.adapter.exceptions.NodeStatusException;
import com.pollalgorand.rest.domain.model.Poll;
import java.time.LocalDateTime;
import org.jmock.Expectations;
import org.jmock.auto.Mock;
import org.jmock.integration.junit4.JUnitRuleMockery;
import org.jmock.lib.legacy.ClassImposteriser;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class UnsignedASCTransactionServiceTest {

  public static final String SENDER_ADDRESS = "GM5YGY4ICDLE27NCVFR6OS7JIIXSGYI6SQIF5IPKQTTGO2YIJU5YOZDP2A";
  public static final String INVALID_SENDER_ADDRESS = "INVALID SENDER ADDRESS";
  public static final String WRONG_SENDER_ADDRESS_ERROR_MESSAGE = "Something went wrong with sender address during transaction creation: A message error";
  public static final long LAST_ROUND = 1L;
  public static byte[] A_BLOCK_NUMBER;
  public static byte[] A_START_SUBS_BLOCK_NUMBER;
  public static byte[] A_END_SUBS_BLOCK_NUMBER;
  public static byte[] A_START_VOTE_BLOCK_NUMBER;
  public static byte[] A_END_VOTE_BLOCK_NUMBER;
  public static final String NODE_STATUS_ERROR_MESSAGE = "Something goes wrong getting node status: AN_ERROR";

  @Rule
  public JUnitRuleMockery context = new JUnitRuleMockery() {{
    setImposteriser(ClassImposteriser.INSTANCE);
  }};

  @Rule
  public final ExpectedException expectedException = none();

  @Mock
  private AlgodClient algodClient;
  @Mock
  private TealProgramFactory tealProgramFactory;
  @Mock
  private PollBlockchainAdapter pollBlockchainAdapter;
  @Mock
  private BuildApplicationCreateTransactionService buildApplicationCreateTransactionService;
  @Mock
  private Response statusResponse;
  @Mock
  private GetStatus getStatus;

  private final String[] headers = {"X-API-Key"};
  private final String[] values = {"KmeYVcOTUFayYL9uVy9mI9d7dDewlWth7pprTlo9"};

  private UnsignedASCTransactionService algorandASCPollRepository;
  private NodeStatusResponse nodeStatusResponse = new NodeStatusResponse();
  private TEALProgram approvalProgram;
  private TEALProgram clearStateProgram;

  @Before
  public void setUp() {

    nodeStatusResponse.lastRound = LAST_ROUND;

    algorandASCPollRepository = new UnsignedASCTransactionService(algodClient,
        pollBlockchainAdapter,
        tealProgramFactory, buildApplicationCreateTransactionService);

    approvalProgram = new TEALProgram();
    clearStateProgram = new TEALProgram();

    A_BLOCK_NUMBER = convertLongToByteArray(1L);
    A_START_SUBS_BLOCK_NUMBER = convertLongToByteArray(1L);
    A_END_SUBS_BLOCK_NUMBER = convertLongToByteArray(2L);
    A_START_VOTE_BLOCK_NUMBER = convertLongToByteArray(3L);
    A_END_VOTE_BLOCK_NUMBER = convertLongToByteArray(4L);
  }

  @Test
  public void createAPollUnsignedTransaction() throws Exception {

    Poll poll = aPollWith(SENDER_ADDRESS);

    PollTealParams pollTealParams = new PollTealParams();

    context.checking(new Expectations() {{

      oneOf(algodClient).GetStatus();
      will(returnValue(getStatus));

      oneOf(getStatus).execute(headers, values);
      will(returnValue(statusResponse));

      oneOf(statusResponse).body();
      will(returnValue(nodeStatusResponse));

      oneOf(pollBlockchainAdapter).fromPollToPollTealParams(poll, LAST_ROUND);
      will(returnValue(pollTealParams));

      oneOf(tealProgramFactory).createApprovalProgramFrom(pollTealParams);
      will(returnValue(approvalProgram));

      oneOf(tealProgramFactory).createClearStateProgram();
      will(returnValue(clearStateProgram));

      oneOf(buildApplicationCreateTransactionService)
          .buildTransaction(pollTealParams, approvalProgram, clearStateProgram, SENDER_ADDRESS);

    }});

    algorandASCPollRepository.createUnsignedTxFor(poll);
  }

  @Test
  public void whenSenderAddressIsNotValid() throws Exception {

    Poll poll = aPollWith(INVALID_SENDER_ADDRESS);

    PollTealParams pollTealParams = new PollTealParams();

    context.checking(new Expectations() {{

      oneOf(algodClient).GetStatus();
      will(returnValue(getStatus));

      oneOf(getStatus).execute(headers, values);
      will(returnValue(statusResponse));

      oneOf(statusResponse).body();
      will(returnValue(nodeStatusResponse));

      oneOf(pollBlockchainAdapter).fromPollToPollTealParams(poll, LAST_ROUND);
      will(returnValue(pollTealParams));

      oneOf(tealProgramFactory).createApprovalProgramFrom(pollTealParams);
      will(returnValue(approvalProgram));

      oneOf(tealProgramFactory).createClearStateProgram();
      will(returnValue(clearStateProgram));

      oneOf(buildApplicationCreateTransactionService)
          .buildTransaction(pollTealParams, approvalProgram, clearStateProgram,
              INVALID_SENDER_ADDRESS);
      will(throwException(
          new InvalidSenderAddressException(new RuntimeException("A message error"))));
    }});

    expectedException.expect(InvalidSenderAddressException.class);
    expectedException.expectMessage(WRONG_SENDER_ADDRESS_ERROR_MESSAGE);

    algorandASCPollRepository.createUnsignedTxFor(poll);

  }

  @Test
  public void whenGettingNodeStatusFails() throws Exception {
    Poll poll = aPollWith(SENDER_ADDRESS);

    context.checking(new Expectations() {{

      oneOf(algodClient).GetStatus();
      will(returnValue(getStatus));

      oneOf(getStatus).execute(headers, values);
      will(throwException(new Exception("AN_ERROR")));

      never(statusResponse);

      never(pollBlockchainAdapter);

      never(tealProgramFactory);

      never(tealProgramFactory);

    }});

    expectedException.expect(NodeStatusException.class);
    expectedException.expectMessage(
        NODE_STATUS_ERROR_MESSAGE);

    algorandASCPollRepository.createUnsignedTxFor(poll);

  }

  private Poll aPollWith(String sender) {
    return new Poll("A_POLL", LocalDateTime.now(), LocalDateTime.now(), LocalDateTime.now(),
        LocalDateTime.now(),
        emptyList(),
        sender, "mnemonicKey", "description");
  }
}