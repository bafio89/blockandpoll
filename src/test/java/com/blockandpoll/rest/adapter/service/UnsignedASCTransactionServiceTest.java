package com.blockandpoll.rest.adapter.service;

import static com.blockandpoll.rest.ByteConverteUtil.convertLongToByteArray;
import static java.util.Collections.emptyList;
import static org.junit.rules.ExpectedException.none;

import com.algorand.algosdk.account.Account;
import com.algorand.algosdk.crypto.TEALProgram;
import com.algorand.algosdk.v2.client.algod.GetStatus;
import com.algorand.algosdk.v2.client.common.AlgodClient;
import com.algorand.algosdk.v2.client.common.Response;
import com.algorand.algosdk.v2.client.model.NodeStatusResponse;
import com.blockandpoll.rest.adapter.PollTealParams;
import com.blockandpoll.rest.adapter.TealProgramFactory;
import com.blockandpoll.rest.adapter.converter.PollBlockchainAdapter;
import com.blockandpoll.rest.adapter.exceptions.NodeStatusException;
import com.blockandpoll.rest.domain.model.Poll;
import java.security.GeneralSecurityException;
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

  public static final String SENDER_ADDRESS = "Q4LQ3VZT2H5YE6RPGXJVHAY32KXBWT527VVTUF75UVSYLMDARDEUNPIN5Y";
  public static final String MNEMONIC_KEY = "share gentle refuse logic shield drift earth initial must match aware they perfect chair say jar harvest echo symbol cave ring void prepare above adult";
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
  private final String[] values = {"INSERT HERE YOUR PURESTAKE API TOKEN"};

  private UnsignedASCTransactionService algorandASCPollRepository;
  private NodeStatusResponse nodeStatusResponse = new NodeStatusResponse();
  private TEALProgram approvalProgram;
  private TEALProgram clearStateProgram;
  private Account account;

  @Before
  public void setUp() throws GeneralSecurityException {

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
    account = new Account(MNEMONIC_KEY);
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

    algorandASCPollRepository.createUnsignedTxFor(poll, account);
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

    algorandASCPollRepository.createUnsignedTxFor(poll, account);

  }

  private Poll aPollWith(String sender) {
    return new Poll("A_POLL", LocalDateTime.now(), LocalDateTime.now(), LocalDateTime.now(),
        LocalDateTime.now(),
        emptyList(),
        sender, "mnemonicKey", "description");
  }
}