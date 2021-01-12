package com.rockandpoll.rest.adapter.service;

import static java.nio.charset.StandardCharsets.UTF_8;
import static java.util.stream.Collectors.joining;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import org.apache.commons.io.IOUtils;

public class TealTextGenerator {

  public static final String OPTIONS_PLACEHOLDER = "OPTIONS_PLACEHOLDER\n";
  private final String APPROVAL_PROGRAM_PATH = "/teal/vote.teal";

  private final String firstOptionTemplate = "txna ApplicationArgs 1\n"
      + "byte \"OPTION\"\n"
      + "==\n";

  private final String optionTextTemplate = "txna ApplicationArgs 1\n"
      + "byte \"OPTION\"\n"
      + "==\n"
      + "||\n";

  public String generateTealTextWithParams(List<String> options) {

    String firstOption = firstOptionTemplate.replace("OPTION", options.get(0));

    String otherOptions = options.stream().skip(1)
        .map(option -> optionTextTemplate.replace("OPTION", option))
        .collect(joining());

    return readFile(APPROVAL_PROGRAM_PATH)
        .replace(OPTIONS_PLACEHOLDER, firstOption.concat(otherOptions));
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
