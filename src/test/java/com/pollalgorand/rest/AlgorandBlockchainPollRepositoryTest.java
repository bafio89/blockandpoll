package com.pollalgorand.rest;

import static java.util.Arrays.asList;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import com.algorand.algosdk.crypto.TEALProgram;
import com.algorand.algosdk.logic.StateSchema;
import com.algorand.algosdk.transaction.Transaction;
import com.algorand.algosdk.v2.client.algod.TransactionParams;
import com.algorand.algosdk.v2.client.common.AlgodClient;
import com.algorand.algosdk.v2.client.common.Response;
import com.algorand.algosdk.v2.client.model.TransactionParametersResponse;
import java.util.Date;
import org.jmock.Expectations;
import org.jmock.auto.Mock;
import org.jmock.integration.junit4.JUnitRuleMockery;
import org.jmock.lib.legacy.ClassImposteriser;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

public class AlgorandBlockchainPollRepositoryTest {

  @Rule
  public JUnitRuleMockery context = new JUnitRuleMockery(){{
    setImposteriser(ClassImposteriser.INSTANCE);
  }};

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

  private AlgorandPollRepository algorandPollRepository;

  private String ALGOD_API_ADDR = "localhost";
  private Integer ALGOD_PORT = 4001;
  private String ALGOD_API_TOKEN = "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa";

  @Before
  public void setUp() {

    AlgodClient algodClient = new AlgodClient(ALGOD_API_ADDR, ALGOD_PORT, ALGOD_API_TOKEN);

    algorandPollRepository = new AlgorandPollRepository(algodClient,
        tealProgramFactory, pollBlockchainParamsAdapter);
  }

  @Test
  public void createAPollUnsignedTransaction() throws Exception {
    TEALProgram approvalProgram = new TEALProgram();
    TEALProgram clearStateProgram = new TEALProgram();

    TransactionParametersResponse transactionParametersResponse = new TransactionParametersResponse();
    transactionParametersResponse.consensusVersion = "";
    transactionParametersResponse.fee = 1L;
    transactionParametersResponse.genesisHash = new byte[32];
    transactionParametersResponse.genesisId = "";
    transactionParametersResponse.lastRound = 1L;

    Transaction expectedTransaction = Transaction.ApplicationCreateTransactionBuilder()
        .sender("GM5YGY4ICDLE27NCVFR6OS7JIIXSGYI6SQIF5IPKQTTGO2YIJU5YOZDP2A")
        .suggestedParams(transactionParametersResponse)
        .approvalProgram(approvalProgram)
        .clearStateProgram(clearStateProgram)
        .globalStateSchema(new StateSchema(1, 0))
        .localStateSchema(new StateSchema(1, 1))
        .build();

    Poll poll = new Poll("A_POLL", new Date(), new Date(), new Date(), new Date(), asList("OPTION_1", "OPTION_2"),
        "A SENDER ADDRESS");

    PollTealParams pollTealParams = new PollTealParams("A SENDER ADDRESS");

    context.checking(new Expectations(){{

      oneOf(algodClient).TransactionParams();
      will(returnValue(transactionParams));

      oneOf(transactionParams).execute();
      will(returnValue(response));

      oneOf(response).body();
      will(returnValue(transactionParametersResponse));

      oneOf(pollBlockchainParamsAdapter).fromPollToTransactionPoll(poll);
      will(returnValue(pollTealParams));

      oneOf(tealProgramFactory).createApprovalProgramFrom(pollTealParams);
      will(returnValue(approvalProgram));

      oneOf(tealProgramFactory).createClearStateProgram();
      will(returnValue(clearStateProgram));

    }});

    Transaction unsignedTx = algorandPollRepository.createUnsignedTx(poll);

    assertThat(unsignedTx, is(expectedTransaction));

  }

}