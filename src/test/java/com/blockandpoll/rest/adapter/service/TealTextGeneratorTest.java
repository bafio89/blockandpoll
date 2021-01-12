package com.blockandpoll.rest.adapter.service;

import static java.util.Arrays.asList;
import static java.util.stream.Collectors.joining;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import org.junit.Before;
import org.junit.Test;

public class TealTextGeneratorTest {

  private final String PATH = "teal/expectedVote.teal";

  private TealTextGenerator tealTextGenerator;

  @Before
  public void setUp() throws Exception {
    tealTextGenerator = new TealTextGenerator();
  }

  @Test
  public void whenPollHasThreeOption() throws IOException, URISyntaxException {

    String expectedTeal = readFile();

    assertThat(
        tealTextGenerator.generateTealTextWithParams(asList("OPTION 1", "OPTION 2", "OPTION 3")),
        is(expectedTeal));
  }

  private String readFile() throws IOException, URISyntaxException {
    return Files.lines(Paths.get(ClassLoader.getSystemResource(PATH).toURI())).collect(
        joining("\n"));
  }
}