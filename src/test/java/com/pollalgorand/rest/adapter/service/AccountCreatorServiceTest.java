package com.pollalgorand.rest.adapter.service;

import static org.junit.rules.ExpectedException.none;

import java.security.GeneralSecurityException;
import org.jmock.integration.junit4.JUnitRuleMockery;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class AccountCreatorServiceTest {

  public static final String SHORT_INVALID_KEY = "INVALID KEY";
  public static final String INVALID_MNEMONIC_KEY = "share gentle refuse logic shield drift earth initial must match aware they perfect chair say jar harvest echo symbol cave ring void prepare above adulq";

  @Rule
  public JUnitRuleMockery context = new JUnitRuleMockery();

  @Rule
  public final ExpectedException expectedException = none();

  private AccountCreatorService accountCreatorService;

  @Before
  public void setUp() {
    accountCreatorService = new AccountCreatorService();
  }

  @Test
  public void whenMnemonicKeyIsTooShort() throws GeneralSecurityException {

    expectedException.expect(IllegalArgumentException.class);

    accountCreatorService.createAccountFrom(SHORT_INVALID_KEY);

  }

  @Test
  public void whenMnemonicKeyIsInvalid() throws GeneralSecurityException {

    expectedException.expect(GeneralSecurityException.class);

    accountCreatorService.createAccountFrom(INVALID_MNEMONIC_KEY);

  }
}