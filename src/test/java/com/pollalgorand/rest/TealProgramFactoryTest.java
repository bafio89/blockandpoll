package com.pollalgorand.rest;

import static com.pollalgorand.rest.ByteConverteUtil.convertLongToByteArray;
import static java.util.Arrays.asList;
import static org.junit.rules.ExpectedException.none;

import com.algorand.algosdk.v2.client.algod.TealCompile;
import com.algorand.algosdk.v2.client.common.AlgodClient;
import com.algorand.algosdk.v2.client.common.Response;
import com.algorand.algosdk.v2.client.model.CompileResponse;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import org.jmock.Expectations;
import org.jmock.auto.Mock;
import org.jmock.integration.junit4.JUnitRuleMockery;
import org.jmock.lib.legacy.ClassImposteriser;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class TealProgramFactoryTest {

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

  public static final String compiledResult = "AiAEAegHgL2DFMDa2yQmASAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAADEQIhIxBygSEDEBIw4QMQgkEjEIJRIREA==";
  public static final String OPTION_1_REPLACED_BY_TEST = "option1_replaced_by_test";
  public static final String OPTION_2_REPLACED_BY_TEST = "option2_replaced_by_test";
  private final String PATH = "teal/vote.teal";
  private TealProgramFactory tealProgramFactory;
  private CompileResponse response;

  @Before
  public void setUp() {
    tealProgramFactory = new TealProgramFactory(algodClient);
    response = new CompileResponse();
  }

  @Test
  public void happyPath() throws Exception {
    byte[] tealProgram = readFile();

    response.result = compiledResult;

    context.checking(new Expectations() {{
      oneOf(algodClient).TealCompile();
      will(returnValue(tealCompile));

      oneOf(tealCompile).source(tealProgram);
      will(returnValue(tealCompile));

      oneOf(tealCompile).execute();
      will(returnValue(compileResponse));

      oneOf(compileResponse).body();
      will(returnValue(response));

    }});

    tealProgramFactory.createApprovalProgramFrom(aPollTealParams());
  }

  @Test
  public void whenClientGoesInError() throws Exception {
    byte[] tealProgram = readFile();

    response.result = compiledResult;

    Exception exception = new Exception("An error message");

    context.checking(new Expectations() {{
      oneOf(algodClient).TealCompile();
      will(returnValue(tealCompile));

      oneOf(tealCompile).source(tealProgram);
      will(returnValue(tealCompile));

      oneOf(tealCompile).execute();
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
        convertLongToByteArray(dateLongRepresentation),
        convertLongToByteArray(dateLongRepresentation),
        convertLongToByteArray(dateLongRepresentation),
        convertLongToByteArray(dateLongRepresentation), asList(
        OPTION_1_REPLACED_BY_TEST, OPTION_2_REPLACED_BY_TEST), "A_SENDER".getBytes());
  }

  private byte[] readFile() throws IOException, URISyntaxException {
    return Files.readAllBytes(Paths.get(ClassLoader.getSystemResource(PATH).toURI()));
  }

}