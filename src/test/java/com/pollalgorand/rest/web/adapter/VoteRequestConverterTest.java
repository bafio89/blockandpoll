package com.pollalgorand.rest.web.adapter;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import com.algorand.algosdk.account.Account;
import com.pollalgorand.rest.adapter.service.AccountCreatorService;
import com.pollalgorand.rest.domain.request.VoteAppRequest;
import com.pollalgorand.rest.web.request.VoteRequest;
import org.jmock.Expectations;
import org.jmock.auto.Mock;
import org.jmock.integration.junit4.JUnitRuleMockery;
import org.jmock.lib.legacy.ClassImposteriser;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

public class VoteRequestConverterTest {

  public static final String AN_OPTION = "AN OPTION";
  public static final String A_MNEMONIC_KEY = "A_MNEMONIC_KEY";
  public static final long APP_ID = 123L;

  @Rule
  public JUnitRuleMockery context = new JUnitRuleMockery(){{
    setImposteriser(ClassImposteriser.INSTANCE);
  }};

  @Mock
  private AccountCreatorService accountCreatorService;

  private VoteRequestConverter voteRequestConverter;

  @Before
  public void setUp() {
    voteRequestConverter = new VoteRequestConverter(accountCreatorService);
  }

  @Test
  public void fromRequestToDomain() throws Exception {

    Account account = new Account();
    VoteAppRequest expectedVoteAppRequest = new VoteAppRequest(APP_ID, account, AN_OPTION);

    context.checking(new Expectations(){{
      oneOf(accountCreatorService).createAccountFrom(A_MNEMONIC_KEY);
      will(returnValue(account));
    }});

    VoteAppRequest voteAppRequest = voteRequestConverter
        .fromRequestToDomain(APP_ID, new VoteRequest(A_MNEMONIC_KEY, AN_OPTION));

    assertThat(expectedVoteAppRequest, is(voteAppRequest));
  }
}