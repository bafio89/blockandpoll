package com.pollalgorand.rest.adapter.service;

import static java.nio.charset.StandardCharsets.UTF_8;

import com.algorand.algosdk.account.Account;
import com.algorand.algosdk.transaction.Transaction;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.pollalgorand.rest.adapter.exceptions.EncodeTransactionException;
import com.pollalgorand.rest.adapter.exceptions.SendingTransactionException;
import com.pollalgorand.rest.adapter.exceptions.SignTransactionException;
import java.security.NoSuchAlgorithmException;
import org.jmock.Expectations;
import org.jmock.auto.Mock;
import org.jmock.integration.junit4.JUnitRuleMockery;
import org.jmock.lib.legacy.ClassImposteriser;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class TransactionWriterServiceTest {

  public static final byte[] A_BYTE_ARRAY = "A_BYTE_ARRAY".getBytes(UTF_8);
  public static final String TRANSACTION_ID = "666";

  @Rule
  public JUnitRuleMockery context = new JUnitRuleMockery() {{
    setImposteriser(ClassImposteriser.INSTANCE);
  }};

  @Rule
  public ExpectedException expectedException = ExpectedException.none();

  @Mock
  private TransactionSignerService transactionSignerService;

  @Mock
  private TransactionSenderService transactionSenderService;

  @Mock
  private TransactionConfirmationService transactionConfirmationService;

  private TransactionWriterService transactionWriterService;
  private Transaction unsignedTx;
  private Account account;

  @Before
  public void setUp() throws NoSuchAlgorithmException {

    transactionWriterService = new TransactionWriterService(transactionSignerService,
        transactionSenderService, transactionConfirmationService);
    unsignedTx = new Transaction();
    account = new Account();
  }

  @Test
  public void signAndSendTransaction() throws Exception {

    context.checking(new Expectations() {{
      oneOf(transactionSignerService).sign(unsignedTx, account);
      will(returnValue(A_BYTE_ARRAY));

      oneOf(transactionSenderService).send(A_BYTE_ARRAY);
      will(returnValue(TRANSACTION_ID));

      allowing(transactionConfirmationService).waitForConfirmation(TRANSACTION_ID);
    }});

    transactionWriterService.write(account, unsignedTx);
  }

  @Test
  public void whenSigningTransactionFails() throws Exception {

    context.checking(new Expectations() {{
      oneOf(transactionSignerService).sign(unsignedTx, account);
      will(throwException(new NoSuchAlgorithmException("ERROR")));

    }});

    expectedException.expect(SignTransactionException.class);
    expectedException.expectMessage("Impossible to sign the transaction. ERROR");

    transactionWriterService.write(account, unsignedTx);
  }

  @Test
  public void whenEncodeSignedTransactionFails() throws Exception {

    context.checking(new Expectations() {{

      oneOf(transactionSignerService).sign(unsignedTx, account);
      will(throwException(new JsonMappingException("ERROR")));

    }});

    expectedException.expect(EncodeTransactionException.class);
    expectedException.expectMessage("Impossible to encode the transaction. ERROR");

    transactionWriterService.write(account, unsignedTx);
  }

  @Test
  public void whenSendTransactionFails() throws Exception {

    Account account = new Account();
    Transaction unsignedTx = new Transaction();

    context.checking(new Expectations() {{

      oneOf(transactionSignerService).sign(unsignedTx, account);
      will(returnValue(A_BYTE_ARRAY));

      oneOf(transactionSenderService).send(A_BYTE_ARRAY);
      will(throwException(new RuntimeException("ERROR")));

      never(transactionConfirmationService);
    }});

    expectedException.expect(SendingTransactionException.class);
    expectedException.expectMessage("Impossible to sign and send the transaction. ERROR");

    transactionWriterService.write(account, unsignedTx);
  }
}