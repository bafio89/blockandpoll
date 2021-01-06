package com.pollalgorand.rest.web.endpoint;

import static java.util.Arrays.asList;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.pollalgorand.rest.domain.ApplicationInfoFromBlockchain;
import com.pollalgorand.rest.domain.model.BlockchainPoll;
import com.pollalgorand.rest.domain.model.EnrichedBlockchainPoll;
import com.pollalgorand.rest.domain.usecase.RetrievePollUseCase;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.HashMap;
import org.jmock.Expectations;
import org.jmock.auto.Mock;
import org.jmock.integration.junit4.JUnitRuleMockery;
import org.jmock.lib.legacy.ClassImposteriser;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

public class ShowPollEndPointTest {

  public static final long APP_ID = 123L;
  public static final String AN_OPTION = "AN_OPTION";
  public static final LocalDateTime now = LocalDateTime.now();
  @Rule
  public JUnitRuleMockery context = new JUnitRuleMockery() {{
    setImposteriser(ClassImposteriser.INSTANCE);
  }};

  @Mock
  private RetrievePollUseCase retrievePollUseCase;

  private MockMvc mockMvc;
  private ObjectMapper objectMapper;

  @Before
  public void setUp() {
    mockMvc = MockMvcBuilders.standaloneSetup(new ShowPollEndPoint(retrievePollUseCase)).build();

    objectMapper = new ObjectMapper();
    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
    objectMapper.setDateFormat(df);
    objectMapper.registerModule(new JavaTimeModule());
  }

  @Test
  public void retrievePollByAppId() throws Exception {

    HashMap<String, BigInteger> optionsVotes = new HashMap<>();
    optionsVotes.put(AN_OPTION, BigInteger.valueOf(2));

    EnrichedBlockchainPoll enrichedBlockchainPoll = new EnrichedBlockchainPoll(expectedBlockchainPoll(),
        new ApplicationInfoFromBlockchain(optionsVotes, 1));

    context.checking(new Expectations(){{
      oneOf(retrievePollUseCase).findPollByAppId(APP_ID);
      will(returnValue(enrichedBlockchainPoll));
    }});

    mockMvc.perform(get("/poll/" + APP_ID)
        .accept(APPLICATION_JSON)
        .contentType(APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(content().string(objectMapper.writeValueAsString(enrichedBlockchainPoll)));
  }


  private BlockchainPoll expectedBlockchainPoll() {
    return new BlockchainPoll(APP_ID, "POLL_NAME", "QUESTION", now, now, now, now,
        asList("OPTION_1", "OPTION_2"), "MEMONIC_KEY", "DESCRIPTION");
  }
}