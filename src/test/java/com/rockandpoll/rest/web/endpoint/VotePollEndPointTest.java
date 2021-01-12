package com.rockandpoll.rest.web.endpoint;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.algorand.algosdk.account.Account;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rockandpoll.rest.domain.exceptions.AlreadyVotedException;
import com.rockandpoll.rest.domain.exceptions.PollNotFoundException;
import com.rockandpoll.rest.domain.request.VoteAppRequest;
import com.rockandpoll.rest.domain.usecase.VoteUseCase;
import com.rockandpoll.rest.web.adapter.VoteRequestConverter;
import com.rockandpoll.rest.web.request.VoteRequest;
import java.security.NoSuchAlgorithmException;
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

  private VoteAppRequest voteAppRequest;

  private MockMvc mockMvc;
  private ObjectMapper objectMapper;

  @Before
  public void setUp() throws NoSuchAlgorithmException {
    mockMvc = MockMvcBuilders
        .standaloneSetup(new VotePollEndPoint(voteUseCase, voteRequestConverter)).build();

    objectMapper = new ObjectMapper();

    voteRequest = new VoteRequest(A_MNEMONIC_KEY, SELECTED_OPTION);

    voteAppRequest = new VoteAppRequest(APP_ID, new Account(), "AN OPTION");
  }

  @Test
  public void happyPath() throws Exception {

    context.checking(new Expectations() {{
      oneOf(voteRequestConverter).fromRequestToDomain(APP_ID, voteRequest);
      will(returnValue(voteAppRequest));

      oneOf(voteUseCase).vote(voteAppRequest);
    }});

    mockMvc.perform(post("/vote/poll/" + APP_ID)
        .accept(APPLICATION_JSON).contentType(APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(voteRequest))).andExpect(status().isOk());
  }

  @Test
  public void whenUserHasAlreadyVoted() throws Exception {

    context.checking(new Expectations() {{
      oneOf(voteRequestConverter).fromRequestToDomain(APP_ID, voteRequest);
      will(returnValue(voteAppRequest));

      oneOf(voteUseCase).vote(voteAppRequest);
      will(throwException(new AlreadyVotedException("AN ADDRESS", APP_ID)));
    }});

    mockMvc.perform(post("/vote/poll/" + APP_ID)
        .accept(APPLICATION_JSON).contentType(APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(voteRequest))).andExpect(status().isPreconditionFailed())
    .andExpect(content().string("Address AN ADDRESS have already voted for appId 123"));
  }

  @Test
  public void whenPollIsNotFound() throws Exception {
    context.checking(new Expectations() {{
      oneOf(voteRequestConverter).fromRequestToDomain(APP_ID, voteRequest);
      will(returnValue(voteAppRequest));

      oneOf(voteUseCase).vote(voteAppRequest);
      will(throwException(new PollNotFoundException(APP_ID)));
    }});

    mockMvc.perform(post("/vote/poll/" + APP_ID)
        .accept(APPLICATION_JSON).contentType(APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(voteRequest))).andExpect(status().isNotFound())
        .andExpect(content().string("Impossible to found the poll with appId: 123"));
  }

  @Test
  public void genericErrorOccurs() throws Exception {

    context.checking(new Expectations() {{
      oneOf(voteRequestConverter).fromRequestToDomain(APP_ID, voteRequest);
      will(returnValue(voteAppRequest));

      oneOf(voteUseCase).vote(voteAppRequest);
      will(throwException(new RuntimeException("AN ERROR OCCURS")));
    }});

    mockMvc.perform(post("/vote/poll/" + APP_ID)
        .accept(APPLICATION_JSON).contentType(APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(voteRequest))).andExpect(status().isInternalServerError())
    .andExpect(content().string("AN ERROR OCCURS"));
  }
}