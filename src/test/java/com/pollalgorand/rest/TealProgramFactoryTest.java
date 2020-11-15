package com.pollalgorand.rest;

import com.google.common.io.CharStreams;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Objects;
import java.util.stream.Collectors;
import org.junit.Test;

public class TealProgramFactoryTest {

  private final String PATH = "teal/vote.teal";

  @Test
  public void happyPath() throws Exception {
    String tealProgram = readFile();

    System.out.println(tealProgram);
  }

  private String readFile() throws IOException, URISyntaxException {
    return Files.lines(Paths.get(ClassLoader.getSystemResource(PATH).toURI())).collect(
          Collectors.joining("\n"));
  }

}