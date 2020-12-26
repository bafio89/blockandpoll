package com.pollalgorand.rest.adapter.service;

import static org.junit.rules.ExpectedException.none;

import com.algorand.algosdk.transaction.Transaction;
import com.fasterxml.jackson.core.JsonProcessingException;
import java.security.GeneralSecurityException;
import org.jmock.integration.junit4.JUnitRuleMockery;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class TransactionSignerServiceTest {

  public static final String SHORT_INVALID_KEY = "INVALID KEY";
  public static final String INVALID_MNEMONIC_KEY = "share gentle refuse logic shield drift earth initial must match aware they perfect chair say jar harvest echo symbol cave ring void prepare above adulq";
  @Rule
  public JUnitRuleMockery context = new JUnitRuleMockery();

  @Rule
  public final ExpectedException expectedException = none();

  private TransactionSignerService transactionSignerService;

  @Before
  public void setUp() {
    transactionSignerService = new TransactionSignerService();
  }

  @Test
  public void whenMnemonicKeyIsTooShort() throws GeneralSecurityException, JsonProcessingException {

    Transaction unsignedTx = new Transaction();

    expectedException.expect(IllegalArgumentException.class);

    transactionSignerService.sign(unsignedTx, SHORT_INVALID_KEY);

  }

  @Test
  public void whenMnemonicKeyIsInvalid() throws GeneralSecurityException, JsonProcessingException {

    Transaction unsignedTx = new Transaction();

    expectedException.expect(GeneralSecurityException.class);

    transactionSignerService.sign(unsignedTx, INVALID_MNEMONIC_KEY);

  }
}