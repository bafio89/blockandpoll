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
  private final String PATH = "teal/vote.teal";

  private AlgodClient algodClient;

  public TealProgramFactory(AlgodClient algodClient) {

    this.algodClient = algodClient;
  }

  public TEALProgram createApprovalProgramFrom(PollTealParams pollTealParams) {

    String tealProgram = readFile().replace(PLACEHOLDER_1, pollTealParams.getOptions().get(0))
        .replace(PLACEHOLDER_2, pollTealParams.getOptions().get(1));

    Response<CompileResponse> compileResponse;
    try {
      compileResponse = algodClient.TealCompile()
          .source(tealProgram.getBytes(UTF_8)).execute();
    } catch (Exception e) {
      throw new RuntimeException();
    }

    return new TEALProgram(compileResponse.body().result);
  }

  public TEALProgram createClearStateProgram() {
    return null;
  }

  private String readFile() {
    try {
      return Files.lines(Paths.get(ClassLoader.getSystemResource(PATH).toURI())).collect(
          joining("\n"));
    } catch (IOException | URISyntaxException e) {
      throw new RuntimeException(e);
    }
  }

}

