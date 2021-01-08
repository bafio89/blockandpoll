package com.pollalgorand.rest.adapter;

import static java.nio.charset.StandardCharsets.UTF_8;

import com.algorand.algosdk.crypto.TEALProgram;
import com.algorand.algosdk.v2.client.common.AlgodClient;
import com.algorand.algosdk.v2.client.common.Response;
import com.algorand.algosdk.v2.client.model.CompileResponse;
import com.pollalgorand.rest.adapter.exceptions.CompileTealProgramException;
import com.pollalgorand.rest.adapter.service.TealTextGenerator;
import java.io.IOException;
import java.io.InputStream;
import org.apache.commons.io.IOUtils;

public class TealProgramFactory {

  private final String CLEAR_STATE_PROGRAM_PATH = "/teal/vote_opt_out.teal";

  private final String[] headers = {"X-API-Key"};
  private final String[] values = {"KmeYVcOTUFayYL9uVy9mI9d7dDewlWth7pprTlo9"};


  private AlgodClient algodClient;
  private TealTextGenerator tealTextGenerator;

  public TealProgramFactory(AlgodClient algodClient,
      TealTextGenerator tealTextGenerator) {

    this.algodClient = algodClient;
    this.tealTextGenerator = tealTextGenerator;
  }

  public TEALProgram createApprovalProgramFrom(PollTealParams pollTealParams) {

    return compileProgram(tealTextGenerator.generateTealTextWithParams(pollTealParams.getOptions()));
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

  private String readFile(String path) {
    try {
      InputStream resourceAsStream = this.getClass().getResourceAsStream(path);
      return IOUtils.toString(resourceAsStream, UTF_8);

    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }
}

