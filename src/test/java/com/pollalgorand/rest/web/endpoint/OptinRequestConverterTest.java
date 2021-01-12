package com.pollalgorand.rest.web.endpoint;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import com.algorand.algosdk.account.Account;
import com.pollalgorand.rest.adapter.exceptions.InvalidMnemonicKeyException;
import com.pollalgorand.rest.adapter.service.AccountCreatorService;
import com.pollalgorand.rest.domain.request.OptinAppRequest;
import com.pollalgorand.rest.web.adapter.OptinRequestConverter;
import com.pollalgorand.rest.web.request.OptinRequest;
import java.security.GeneralSecurityException;
import org.jmock.Expectations;
import org.jmock.auto.Mock;
import org.jmock.integration.junit4.JUnitRuleMockery;
import org.jmock.lib.legacy.ClassImposteriser;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class OptinRequestConverterTest {

  public static final String A_MNEMONIC_KEY = "A_MNEMONIC_KEY";
  @Rule
  public JUnitRuleMockery context = new JUnitRuleMockery() {{
    setImposteriser(ClassImposteriser.INSTANCE);
  }};

  @Rule
  public ExpectedException expectedException = ExpectedException.none();

  @Mock
  private AccountCreatorService accountCreatorService;

  private OptinRequestConverter optinRequestConverter;

  @Before
  public void setUp() {
    optinRequestConverter = new OptinRequestConverter(accountCreatorService);
  }

  @Test
  public void happyPath() throws GeneralSecurityException {

    Account expectedAccount = new Account();
    OptinAppRequest expectedRequest = new OptinAppRequest(123L, expectedAccount);

    context.checking(new Expectations() {{
      oneOf(accountCreatorService).createAccountFrom(A_MNEMONIC_KEY);
      will(returnValue(expectedAccount));
    }});

    OptinAppRequest request = optinRequestConverter.fromRequestToDomain(123L, new OptinRequest(A_MNEMONIC_KEY));

    assertThat(expectedRequest, is(request));
  }

  @Test
  public void whenAccountCreationFails() throws GeneralSecurityException {

    Account expectedAccount = new Account();

    context.checking(new Expectations() {{
      oneOf(accountCreatorService).createAccountFrom(A_MNEMONIC_KEY);
      will(throwException(new InvalidMnemonicKeyException("AN ERROR")));
    }});

    expectedException.expect(InvalidMnemonicKeyException.class);
    expectedException.expectMessage("AN ERROR");

    optinRequestConverter.fromRequestToDomain(123L, new OptinRequest(A_MNEMONIC_KEY));
  }
}