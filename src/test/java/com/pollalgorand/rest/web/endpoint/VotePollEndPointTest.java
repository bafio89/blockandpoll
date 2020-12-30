package com.pollalgorand.rest.web.endpoint;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pollalgorand.rest.domain.request.VoteAppRequest;
import com.pollalgorand.rest.domain.usecase.VoteUseCase;
import com.pollalgorand.rest.web.adapter.VoteRequestConverter;
import com.pollalgorand.rest.web.request.VoteRequest;
import org.jmock.Expectations;
import org.jmock.auto.Mock;
import org.jmock.integration.junit4.JUnitRuleMockery;
import org.jmock.lib.legacy.ClassImposteriser;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

public class VotePollEndPointTest {

  private static final long APP_ID = 123;
  private static final String A_MNEMONIC_KEY = "share gentle refuse logic shield drift earth initial must match aware they perfect chair say jar harvest echo symbol cave ring void prepare above adult";
  private static final String SELECTED_OPTION = "AN_OPTION";

  @Rule
  public JUnitRuleMockery context = new JUnitRuleMockery() {{
    setImposteriser(ClassImposteriser.INSTANCE);
  }};

  @Mock
  private VoteUseCase voteUseCase;

  @Mock
  private VoteRequestConverter voteRequestConverter;

  private VoteRequest voteRequest;

  private MockMvc mockMvc;
  private ObjectMapper objectMapper;

  @Before
  public void setUp() {
    mockMvc = MockMvcBuilders
        .standaloneSetup(new VotePollEndPoint(voteUseCase, voteRequestConverter)).build();

    objectMapper = new ObjectMapper();

    voteRequest = new VoteRequest(A_MNEMONIC_KEY, SELECTED_OPTION);
  }

  @Test
  public void happyPath() throws Exception {

    VoteAppRequest voteAppRequest = new VoteAppRequest();

    context.checking(new Expectations() {{
      oneOf(voteRequestConverter).fromRequestToDomain(APP_ID, voteRequest);
      will(returnValue(voteAppRequest));

      oneOf(voteUseCase).vote(voteAppRequest);
    }});

    mockMvc.perform(post("/vote/poll/" + APP_ID)
        .accept(APPLICATION_JSON).contentType(APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(voteRequest))).andExpect(status().isOk());
  }
}