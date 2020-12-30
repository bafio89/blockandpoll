package com.pollalgorand.rest.web.endpoint;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.algorand.algosdk.account.Account;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pollalgorand.rest.domain.exceptions.OptinAlreadyDoneException;
import com.pollalgorand.rest.domain.request.OptinAppRequest;
import com.pollalgorand.rest.domain.usecase.OptinUseCase;
import com.pollalgorand.rest.web.request.OptinRequest;
import org.jmock.Expectations;
import org.jmock.auto.Mock;
import org.jmock.integration.junit4.JUnitRuleMockery;
import org.jmock.lib.legacy.ClassImposteriser;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

public class OptinPollEndPointTest {

  private static final int APP_ID = 123;
  private static final String A_MNEMONIC_KEY = "share gentle refuse logic shield drift earth initial must match aware they perfect chair say jar harvest echo symbol cave ring void prepare above adult";
  @Rule
  public JUnitRuleMockery context = new JUnitRuleMockery() {{
    setImposteriser(ClassImposteriser.INSTANCE);
  }};

  @Mock
  private OptinUseCase optinUseCase;

  @Mock
  private OptinRequestConverter optinRequestConverter;

  private MockMvc mockMvc;
  private ObjectMapper objectMapper;
  private OptinRequest optinRequest;

  @Before
  public void setUp() {
    mockMvc = MockMvcBuilders.standaloneSetup(new OptinPollEndPoint(optinUseCase, optinRequestConverter)).build();

    objectMapper = new ObjectMapper();
    optinRequest = new OptinRequest(A_MNEMONIC_KEY);
  }

  @Test
  public void happyPath() throws Exception {

    OptinAppRequest optinAppRequest = new OptinAppRequest(APP_ID, new Account(A_MNEMONIC_KEY));

    context.checking(new Expectations(){{
      oneOf(optinRequestConverter).fromRequestToDomain(APP_ID, new OptinRequest(A_MNEMONIC_KEY));
      will(returnValue(optinAppRequest));
      oneOf(optinUseCase).optin(optinAppRequest);
    }});

    mockMvc.perform(post("/optin/poll/" + APP_ID)
        .accept(APPLICATION_JSON).contentType(APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(optinRequest))).andExpect(status().isOk());

  }

  @Test
  public void whenOptinHasBeenAlreadyDone() throws Exception {

    OptinAppRequest optinAppRequest = new OptinAppRequest(APP_ID, new Account(A_MNEMONIC_KEY));

    context.checking(new Expectations(){{
      oneOf(optinRequestConverter).fromRequestToDomain(APP_ID, new OptinRequest(A_MNEMONIC_KEY));
      will(returnValue(optinAppRequest));
      oneOf(optinUseCase).optin(optinAppRequest);
      will(throwException(new OptinAlreadyDoneException(optinAppRequest.getAppId())));
    }});

    mockMvc.perform(post("/optin/poll/" + APP_ID)
        .accept(APPLICATION_JSON).contentType(APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(optinRequest))).andExpect(status().isPreconditionFailed());

  }

  @Test
  public void genericError() throws Exception{

    OptinAppRequest optinAppRequest = new OptinAppRequest(APP_ID, new Account(A_MNEMONIC_KEY));

    context.checking(new Expectations(){{
      oneOf(optinRequestConverter).fromRequestToDomain(APP_ID, new OptinRequest(A_MNEMONIC_KEY));
      will(returnValue(optinAppRequest));
      oneOf(optinUseCase).optin(optinAppRequest);
      will(throwException(new RuntimeException()));
    }});

    mockMvc.perform(post("/optin/poll/" + APP_ID)
        .accept(APPLICATION_JSON).contentType(APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(optinRequest))).andExpect(status().is5xxServerError());

  }
}