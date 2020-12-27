package com.pollalgorand.rest.adapter.repository;

import static java.nio.charset.StandardCharsets.UTF_8;

import com.algorand.algosdk.account.Account;
import com.algorand.algosdk.transaction.Transaction;
import com.algorand.algosdk.v2.client.common.AlgodClient;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.pollalgorand.rest.adapter.exceptions.EncodeTransactionException;
import com.pollalgorand.rest.adapter.exceptions.InvalidMnemonicKeyException;
import com.pollalgorand.rest.adapter.exceptions.SendingTransactionException;
import com.pollalgorand.rest.adapter.exceptions.SignTransactionException;
import com.pollalgorand.rest.adapter.service.AccountCreatorService;
import com.pollalgorand.rest.adapter.service.BuildOptinTransactionService;
import com.pollalgorand.rest.adapter.service.TransactionConfirmationService;
import com.pollalgorand.rest.adapter.service.TransactionSenderService;
import com.pollalgorand.rest.adapter.service.TransactionSignerService;
import com.pollalgorand.rest.domain.OptinAppRequest;
import com.pollalgorand.rest.domain.repository.BlockchainWriteRepository;
import java.security.GeneralSecurityException;
import java.security.NoSuchAlgorithmException;
import org.jmock.Expectations;
import org.jmock.auto.Mock;
import org.jmock.integration.junit4.JUnitRuleMockery;
import org.jmock.lib.legacy.ClassImposteriser;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class AlgorandWriteRepositoryTest {

  public static final String A_MNEMONIC_KEY = "A_MNEMONIC_KEY";
  public static final byte[] A_BYTE_ARRAY = "A_BYTE_ARRAY".getBytes(UTF_8);
  public static final String TRANSACTION_ID = "666";

  @Rule
  public JUnitRuleMockery context = new JUnitRuleMockery() {{
    setImposteriser(ClassImposteriser.INSTANCE);
  }};

  @Rule
  public ExpectedException expectedException = ExpectedException.none();

  @Mock
  public AlgodClient algodClient;

  @Mock
  public BuildOptinTransactionService buildOptinTransactionService;

  @Mock
  public AccountCreatorService accountCreator;

  @Mock
  public TransactionSignerService transactionSignerService;

  @Mock
  public TransactionSenderService transactionSenderService;

  @Mock
  public TransactionConfirmationService transactionConfirmationService;


  private BlockchainWriteRepository blockchainWriteRepository;

  @Before
  public void setUp() {
    blockchainWriteRepository = new AlgorandWriteRepository(accountCreator,
        buildOptinTransactionService, transactionSignerService, transactionSenderService,
        transactionConfirmationService);
  }

  @Test
  public void happyPath() throws Exception {

    Account account = new Account();
    OptinAppRequest optinAppRequest = new OptinAppRequest(123L, A_MNEMONIC_KEY);
    Transaction unsignedTx = new Transaction();

    context.checking(new Expectations() {{
      oneOf(accountCreator).createAccountFrom(A_MNEMONIC_KEY);
      will(returnValue(account));

      oneOf(buildOptinTransactionService).buildTransaction(account, optinAppRequest);
      will(returnValue(unsignedTx));

      oneOf(transactionSignerService).sign(unsignedTx, account);
      will(returnValue(A_BYTE_ARRAY));

      oneOf(transactionSenderService).send(A_BYTE_ARRAY);
      will(returnValue(TRANSACTION_ID));

      allowing(transactionConfirmationService).waitForConfirmation(TRANSACTION_ID);
    }});

    blockchainWriteRepository.optin(optinAppRequest);
  }

  @Test
  public void whenAccountCreationFails() throws Exception {
    OptinAppRequest optinAppRequest = new OptinAppRequest(123L, A_MNEMONIC_KEY);

    context.checking(new Expectations() {{
      oneOf(accountCreator).createAccountFrom(A_MNEMONIC_KEY);
      will(throwException(new GeneralSecurityException("AN ERROR")));

    }});

    expectedException.expect(InvalidMnemonicKeyException.class);
    expectedException
        .expectMessage("Impossible to create an account starting from mnemonic key. AN ERROR");

    blockchainWriteRepository.optin(optinAppRequest);
  }

  @Test
  public void whenSigningTransactionFails() throws Exception {
    Account account = new Account();
    OptinAppRequest optinAppRequest = new OptinAppRequest(123L, A_MNEMONIC_KEY);
    Transaction unsignedTx = new Transaction();

    context.checking(new Expectations() {{
      oneOf(accountCreator).createAccountFrom(A_MNEMONIC_KEY);
      will(returnValue(account));

      oneOf(buildOptinTransactionService).buildTransaction(account, optinAppRequest);
      will(returnValue(unsignedTx));

      oneOf(transactionSignerService).sign(unsignedTx, account);
      will(throwException(new NoSuchAlgorithmException("ERROR")));

    }});

    expectedException.expect(SignTransactionException.class);
    expectedException.expectMessage("Impossible to sign the transaction. ERROR");

    blockchainWriteRepository.optin(optinAppRequest);
  }

  @Test
  public void whenEncodeSignedTransactionFails() throws Exception {
    Account account = new Account();
    OptinAppRequest optinAppRequest = new OptinAppRequest(123L, A_MNEMONIC_KEY);
    Transaction unsignedTx = new Transaction();

    context.checking(new Expectations() {{
      oneOf(accountCreator).createAccountFrom(A_MNEMONIC_KEY);
      will(returnValue(account));

      oneOf(buildOptinTransactionService).buildTransaction(account, optinAppRequest);
      will(returnValue(unsignedTx));

      oneOf(transactionSignerService).sign(unsignedTx, account);
      will(throwException(new JsonMappingException("ERROR")));

    }});

    expectedException.expect(EncodeTransactionException.class);
    expectedException.expectMessage("Impossible to encode the transaction. ERROR");

    blockchainWriteRepository.optin(optinAppRequest);
  }

  @Test
  public void whenSendTranctionFails() throws Exception {

    Account account = new Account();
    OptinAppRequest optinAppRequest = new OptinAppRequest(123L, A_MNEMONIC_KEY);
    Transaction unsignedTx = new Transaction();

    context.checking(new Expectations() {{
      oneOf(accountCreator).createAccountFrom(A_MNEMONIC_KEY);
      will(returnValue(account));

      oneOf(buildOptinTransactionService).buildTransaction(account, optinAppRequest);
      will(returnValue(unsignedTx));

      oneOf(transactionSignerService).sign(unsignedTx, account);
      will(returnValue(A_BYTE_ARRAY));

      oneOf(transactionSenderService).send(A_BYTE_ARRAY);
      will(throwException(new RuntimeException("ERROR")));

      allowing(transactionConfirmationService).waitForConfirmation(TRANSACTION_ID);
    }});

    expectedException.expect(SendingTransactionException.class);
    expectedException.expectMessage("Impossible to sign and send the transaction. ERROR");

    blockchainWriteRepository.optin(optinAppRequest);
  }
}