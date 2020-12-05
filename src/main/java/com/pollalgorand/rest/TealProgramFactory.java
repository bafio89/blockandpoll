package com.pollalgorand.rest;

import static java.nio.charset.StandardCharsets.UTF_8;
import static java.util.stream.Collectors.joining;

import com.algorand.algosdk.crypto.TEALProgram;
import com.algorand.algosdk.v2.client.common.AlgodClient;
import com.algorand.algosdk.v2.client.common.Response;
import com.algorand.algosdk.v2.client.model.CompileResponse;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class TealProgramFactory {

  public static final String PLACEHOLDER_1 = "OPTION_1";
  public static final String PLACEHOLDER_2 = "OPTION_2";
  private final String APPROVAL_PROGRAM_PATH = "teal/vote.teal";
  private final String CLEAR_STATE_PROGRAM_PATH = "teal/vote_opt_out.teal";

  private final String[] headers = {"X-API-Key"};
  private final String[] values = {"KmeYVcOTUFayYL9uVy9mI9d7dDewlWth7pprTlo9"};


  private AlgodClient algodClient;

  public TealProgramFactory(AlgodClient algodClient) {

    this.algodClient = algodClient;
  }

  public TEALProgram createApprovalProgramFrom(PollTealParams pollTealParams) {

    String tealProgramAsString = readFile(APPROVAL_PROGRAM_PATH)
        .replace(PLACEHOLDER_1, pollTealParams.getOptions().get(0))
        .replace(PLACEHOLDER_2, pollTealParams.getOptions().get(1));

    return compileProgram(tealProgramAsString);
  }

  public TEALProgram createClearStateProgram() {

    String clearStateProgramAsString = readFile(CLEAR_STATE_PROGRAM_PATH);
    return compileProgram(clearStateProgramAsString);

  }

  private TEALProgram compileProgram(String tealProgramAsStream) {
    Response<CompileResponse> compileResponse;
    try {
      compileResponse = algodClient.TealCompile()
          .source(tealProgramAsStream.getBytes(UTF_8)).execute(headers, values);
    } catch (Exception e) {
      throw new CompileTealProgramException(e);
    }

    return new TEALProgram(compileResponse.body().result);
  }

  private String readFile(String PATH) {
    try {
      return Files.lines(Paths.get(ClassLoader.getSystemResource(PATH).toURI())).collect(
          joining("\n"));
    } catch (IOException | URISyntaxException e) {
      throw new RuntimeException(e);
    }
  }

}

