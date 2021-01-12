package com.rockandpoll.rest;

import static com.rockandpoll.rest.adapter.AlgorandUtils.headers;
import static com.rockandpoll.rest.adapter.AlgorandUtils.values;
import static java.nio.charset.StandardCharsets.UTF_8;
import static java.util.Arrays.asList;
import static org.junit.rules.ExpectedException.none;

import com.algorand.algosdk.v2.client.algod.TealCompile;
import com.algorand.algosdk.v2.client.common.AlgodClient;
import com.algorand.algosdk.v2.client.common.Response;
import com.algorand.algosdk.v2.client.model.CompileResponse;
import com.rockandpoll.rest.adapter.PollTealParams;
import com.rockandpoll.rest.adapter.TealProgramFactory;
import com.rockandpoll.rest.adapter.exceptions.CompileTealProgramException;
import com.rockandpoll.rest.adapter.service.TealTextGenerator;
import org.jmock.Expectations;
import org.jmock.auto.Mock;
import org.jmock.integration.junit4.JUnitRuleMockery;
import org.jmock.lib.legacy.ClassImposteriser;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class TealProgramFactoryTest {

  public static final String A_TEAL_PROGRAM = "A TEAL PROGRAM";
  @Rule
  public JUnitRuleMockery context = new JUnitRuleMockery() {{
    setImposteriser(ClassImposteriser.INSTANCE);
  }};

  @Rule
  public final ExpectedException expectedException = none();

  @Mock
  private AlgodClient algodClient;

  @Mock
  private TealCompile tealCompile;

  @Mock
  private Response compileResponse;

  @Mock
  private TealTextGenerator tealTextGenerator;

  public static final String compiledResult = "AiAEAegHgL2DFMDa2yQmASAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAADEQIhIxBygSEDEBIw4QMQgkEjEIJRIREA==";
  public static final String OPTION_1_REPLACED_BY_TEST = "option1_replaced_by_test";
  public static final String OPTION_2_REPLACED_BY_TEST = "option2_replaced_by_test";
  private TealProgramFactory tealProgramFactory;
  private CompileResponse response;

  @Before
  public void setUp() {
    tealProgramFactory = new TealProgramFactory(algodClient, tealTextGenerator);
    response = new CompileResponse();
  }

  @Test
  public void happyPath() throws Exception {

    response.result = compiledResult;

    context.checking(new Expectations() {{
      oneOf(tealTextGenerator)
          .generateTealTextWithParams(asList(OPTION_1_REPLACED_BY_TEST, OPTION_2_REPLACED_BY_TEST));
      will(returnValue(A_TEAL_PROGRAM));

      oneOf(algodClient).TealCompile();
      will(returnValue(tealCompile));

      oneOf(tealCompile).source(A_TEAL_PROGRAM.getBytes(UTF_8));
      will(returnValue(tealCompile));

      oneOf(tealCompile).execute(headers, values);
      will(returnValue(compileResponse));

      oneOf(compileResponse).body();
      will(returnValue(response));

    }});

    tealProgramFactory.createApprovalProgramFrom(aPollTealParams());
  }

  @Test
  public void whenClientGoesInError() throws Exception {

    response.result = compiledResult;

    Exception exception = new Exception("An error message");

    context.checking(new Expectations() {{
      oneOf(tealTextGenerator)
          .generateTealTextWithParams(asList(OPTION_1_REPLACED_BY_TEST, OPTION_2_REPLACED_BY_TEST));
      will(returnValue(A_TEAL_PROGRAM));

      oneOf(algodClient).TealCompile();
      will(returnValue(tealCompile));

      oneOf(tealCompile).source(A_TEAL_PROGRAM.getBytes(UTF_8));
      will(returnValue(tealCompile));

      oneOf(tealCompile).execute(headers, values);
      will(throwException(exception));
    }});

    expectedException.expect(CompileTealProgramException.class);
    expectedException
        .expectMessage("Something goes wrong during TEAL program compilation: An error message");

    tealProgramFactory.createApprovalProgramFrom(aPollTealParams());

  }

  private PollTealParams aPollTealParams() {
    long dateLongRepresentation = 123L;
    return new PollTealParams("A_NAME".getBytes(),
        ByteConverteUtil.convertLongToByteArray(dateLongRepresentation),
        ByteConverteUtil.convertLongToByteArray(dateLongRepresentation),
        ByteConverteUtil.convertLongToByteArray(dateLongRepresentation),
        ByteConverteUtil.convertLongToByteArray(dateLongRepresentation), asList(
        OPTION_1_REPLACED_BY_TEST, OPTION_2_REPLACED_BY_TEST), "A_SENDER".getBytes());
  }

}