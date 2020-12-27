package com.pollalgorand.rest.web.endpoint;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pollalgorand.rest.domain.OptinAppRequest;
import com.pollalgorand.rest.domain.exceptions.OptinAlreadDoneException;
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

  public static final int APP_ID = 123;
  public static final String A_MNEMONIC_KEY = "A_MNEMONIC_KEY";
  public static final String A_SENDER = "A_SENDER";
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

  @Before
  public void setUp() {
    mockMvc = MockMvcBuilders.standaloneSetup(new OptinPollEndPoint(optinUseCase, optinRequestConverter)).build();

    objectMapper = new ObjectMapper();
  }

  @Test
  public void happyPath() throws Exception {

    OptinRequest optinRequest = new OptinRequest(A_SENDER, A_MNEMONIC_KEY);

    OptinAppRequest optinAppRequest = new OptinAppRequest(APP_ID, A_MNEMONIC_KEY);

    context.checking(new Expectations(){{
      oneOf(optinRequestConverter).fromRequestToDomain(APP_ID, optinRequest);
      will(returnValue(optinAppRequest));
      oneOf(optinUseCase).optin(optinAppRequest);
    }});

    String requestAsString = objectMapper.writeValueAsString(optinRequest);
    mockMvc.perform(post("/optin/poll/" + APP_ID)
        .accept(APPLICATION_JSON).contentType(APPLICATION_JSON)
        .content(requestAsString)).andExpect(status().isOk());

  }

  @Test
  public void whenOptinHasBeenAlreadyDone() throws Exception {

    OptinRequest optinRequest = new OptinRequest(A_SENDER, A_MNEMONIC_KEY);

    OptinAppRequest optinAppRequest = new OptinAppRequest(APP_ID, A_MNEMONIC_KEY);

    context.checking(new Expectations(){{
      oneOf(optinRequestConverter).fromRequestToDomain(APP_ID, optinRequest);
      will(returnValue(optinAppRequest));
      oneOf(optinUseCase).optin(optinAppRequest);
      will(throwException(new OptinAlreadDoneException(optinAppRequest.getAppId())));
    }});

    String requestAsString = objectMapper.writeValueAsString(optinRequest);
    mockMvc.perform(post("/optin/poll/" + APP_ID)
        .accept(APPLICATION_JSON).contentType(APPLICATION_JSON)
        .content(requestAsString)).andExpect(status().isPreconditionFailed());

  }

  @Test
  public void genericError() throws Exception{
    OptinRequest optinRequest = new OptinRequest(A_SENDER, A_MNEMONIC_KEY);

    OptinAppRequest optinAppRequest = new OptinAppRequest(APP_ID, A_MNEMONIC_KEY);

    context.checking(new Expectations(){{
      oneOf(optinRequestConverter).fromRequestToDomain(APP_ID, optinRequest);
      will(returnValue(optinAppRequest));
      oneOf(optinUseCase).optin(optinAppRequest);
      will(throwException(new RuntimeException()));
    }});

    String requestAsString = objectMapper.writeValueAsString(optinRequest);
    mockMvc.perform(post("/optin/poll/" + APP_ID)
        .accept(APPLICATION_JSON).contentType(APPLICATION_JSON)
        .content(requestAsString)).andExpect(status().is5xxServerError());

  }
}