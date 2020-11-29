package com.pollalgorand.rest;


import static java.util.Collections.singletonList;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import org.jmock.Expectations;
import org.jmock.auto.Mock;
import org.jmock.integration.junit4.JUnitRuleMockery;
import org.jmock.lib.legacy.ClassImposteriser;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

public class PollEndPointTest {

  @Rule
  public JUnitRuleMockery context = new JUnitRuleMockery(){{
      setImposteriser(ClassImposteriser.INSTANCE);
  }};

  @Mock
  private CreatePollUseCase createPollUseCase;

  private MockMvc mockMvc;
  private ObjectMapper objectMapper;

  @Before
  public void setUp() {
    mockMvc = MockMvcBuilders.standaloneSetup(
        new PollEndPoint(createPollUseCase, new PollRequestAdapter())
    ).build();

    objectMapper = new ObjectMapper();
    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm");
    objectMapper.setDateFormat(df);
    objectMapper.registerModule(new JavaTimeModule());
  }

  @Test
  public void whenPollParametersAreInvalid() throws Exception {

    LocalDateTime now = LocalDateTime.of(2020, 11, 30, 0,0);
    PollRequest pollRequest = aPollRequest(now);
    Poll poll = aPoll(now);

    context.checking(new Expectations(){{
      oneOf(createPollUseCase).createUnsignedTx(poll);
      will(throwException(new IllegalPollParameterException("A MESSAGE")));
    }});

    String content = objectMapper.writeValueAsString(pollRequest);
    mockMvc.perform(MockMvcRequestBuilders.post("/createpoll/unsignedtx")
        .accept(APPLICATION_JSON).contentType(APPLICATION_JSON)
        .content(content))
        .andExpect(status().isPreconditionFailed())
        .andExpect(content().string("Invalid poll parameters: A MESSAGE"));

  }

  @Test
  public void whenThereIsAnInternalServerError() throws Exception {

    LocalDateTime now = LocalDateTime.of(2020, 11, 30, 0,0);
    PollRequest pollRequest = aPollRequest(now);
    Poll poll = aPoll(now);

    context.checking(new Expectations(){{
      oneOf(createPollUseCase).createUnsignedTx(poll);
      will(throwException(new RuntimeException("A MESSAGE")));
    }});

    String content = objectMapper.writeValueAsString(pollRequest);
    mockMvc.perform(MockMvcRequestBuilders.post("/createpoll/unsignedtx")
        .accept(APPLICATION_JSON).contentType(APPLICATION_JSON)
        .content(content))
        .andExpect(status().isInternalServerError())
        .andExpect(content().string("A MESSAGE"));
  }

  private PollRequest aPollRequest(LocalDateTime now) {
    return new PollRequest("A NAME",
        now, now, now, now,
        singletonList("AN OPTION"),
        "A SENDER");
  }

  private Poll aPoll(LocalDateTime now){
    return new Poll("A NAME",
        now, now, now, now,
        singletonList("AN OPTION"),
        "A SENDER");
  }
}