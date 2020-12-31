package com.pollalgorand.rest.adapter.repository;

import static com.pollalgorand.rest.adapter.AlgorandUtils.txHeaders;
import static com.pollalgorand.rest.adapter.AlgorandUtils.txValues;
import static java.nio.charset.StandardCharsets.UTF_8;
import static org.junit.rules.ExpectedException.none;

import com.algorand.algosdk.account.Account;
import com.algorand.algosdk.transaction.Transaction;
import com.algorand.algosdk.v2.client.algod.RawTransaction;
import com.algorand.algosdk.v2.client.common.AlgodClient;
import com.algorand.algosdk.v2.client.common.Response;
import com.algorand.algosdk.v2.client.model.PostTransactionsResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.pollalgorand.rest.adapter.converter.PollBlockchainAdapter;
import com.pollalgorand.rest.adapter.exceptions.EncodeTransactionException;
import com.pollalgorand.rest.adapter.exceptions.InvalidMnemonicKeyException;
import com.pollalgorand.rest.adapter.exceptions.SendingTransactionException;
import com.pollalgorand.rest.adapter.exceptions.SignTransactionException;
import com.pollalgorand.rest.adapter.service.AccountCreatorService;
import com.pollalgorand.rest.adapter.service.AlgorandApplicationService;
import com.pollalgorand.rest.adapter.service.TransactionConfirmationService;
import com.pollalgorand.rest.adapter.service.TransactionSignerService;
import com.pollalgorand.rest.adapter.service.UnsignedASCTransactionService;
import com.pollalgorand.rest.domain.model.Poll;
import java.security.GeneralSecurityException;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.util.Collections;
import org.jmock.Expectations;
import org.jmock.auto.Mock;
import org.jmock.integration.junit4.JUnitRuleMockery;
import org.jmock.lib.legacy.ClassImposteriser;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class AlgorandASCPollRepositoryTest {

  public static final String A_MNEMONIC_KEY = "A_MNEMONIC_KEY";
  public static final String TX_ID = "999";
  public static final long A_TRANSACTION_ID = 123L;
  public static final String A_POLL_NAME = "A_POLL_NAME";
  public static final byte[] A_BYTE_ARRAY = "A_BYTE_ARRAY".getBytes(UTF_8);

  @Rule
  public JUnitRuleMockery context = new JUnitRuleMockery() {{
    setImposteriser(ClassImposteriser.INSTANCE);
  }};

  @Rule
  public final ExpectedException expectedException = none();

  @Mock
  private AlgodClient algodClient;

  @Mock
  private TransactionSignerService transactionSignerService;

  @Mock
  private UnsignedASCTransactionService unsignedASCTransactionService;

  @Mock
  private RawTransaction rawTransaction;

  @Mock
  private Response executeResponse;

  @Mock
  private TransactionConfirmationService transactionConfirmationService;

  @Mock
  private AccountCreatorService accountCreatorService;

  @Mock
  private PollBlockchainAdapter pollBlockchainAdapter;

  @Mock
  private AlgorandApplicationService algorandApplicationService;

  private AlgorandASCPollRepository algorandASCPollRepository;
  private Account account;

  @Before
  public void setUp() throws NoSuchAlgorithmException {
    algorandASCPollRepository = new AlgorandASCPollRepository(algodClient, accountCreatorService, transactionSignerService,
        unsignedASCTransactionService, pollBlockchainAdapter, transactionConfirmationService,
        algorandApplicationService);

    account = new Account();
  }

  @Test
  public void happyPath() throws Exception {

    Poll poll = poll();
    Transaction unsignedTx = new Transaction();

    PostTransactionsResponse postTransactionResponse = new PostTransactionsResponse();
    postTransactionResponse.txId = TX_ID;

    context.checking(new Expectations() {{
      oneOf(unsignedASCTransactionService).createUnsignedTxFor(poll);
      will(returnValue(unsignedTx));

      oneOf(accountCreatorService).createAccountFrom(A_MNEMONIC_KEY);
      will(returnValue(account));

      oneOf(transactionSignerService).sign(unsignedTx, account);
      will(returnValue(A_BYTE_ARRAY));

      oneOf(algodClient).RawTransaction();
      will(returnValue(rawTransaction));

      oneOf(rawTransaction).rawtxn(A_BYTE_ARRAY);
      will(returnValue(rawTransaction));

      oneOf(rawTransaction).execute(txHeaders, txValues);
      will(returnValue(executeResponse));

      oneOf(executeResponse).body();
      will(returnValue(postTransactionResponse));

      allowing(transactionConfirmationService).waitForConfirmation(TX_ID);

      oneOf(algorandApplicationService).getApplicationId(TX_ID);
      will(returnValue(A_TRANSACTION_ID));

      oneOf(pollBlockchainAdapter).fromPollToBlockchainPoll(poll, A_TRANSACTION_ID);
    }});

    algorandASCPollRepository.save(poll);
  }

  @Test
  public void whenThereAreSigningError() throws GeneralSecurityException, JsonProcessingException {

    Poll poll = poll();
    Transaction unsignedTx = new Transaction();

    PostTransactionsResponse postTransactionResponse = new PostTransactionsResponse();
    postTransactionResponse.txId = TX_ID;

    context.checking(new Expectations() {
      {
        oneOf(unsignedASCTransactionService).createUnsignedTxFor(poll);
        will(returnValue(unsignedTx));

        oneOf(accountCreatorService).createAccountFrom(A_MNEMONIC_KEY);
        will(returnValue(account));

        oneOf(transactionSignerService).sign(unsignedTx, account);
        will(throwException(new NoSuchAlgorithmException("AN ERROR MESSAGE")));
      }
    });

    expectedException.expect(SignTransactionException.class);
    expectedException.expectMessage("Impossible to sign the transaction. AN ERROR MESSAGE");

    algorandASCPollRepository.save(poll);
  }

  @Test
  public void whenThereAreErrorsCreatingAccount() {
    Poll poll = poll();
    Transaction unsignedTx = new Transaction();

    context.checking(new Expectations() {
      {
        oneOf(unsignedASCTransactionService).createUnsignedTxFor(poll);
        will(returnValue(unsignedTx));

        oneOf(accountCreatorService).createAccountFrom(A_MNEMONIC_KEY);
        will(throwException(new IllegalArgumentException("AN ERROR MESSAGE")));

        never(transactionSignerService);
      }
    });

    expectedException.expect(InvalidMnemonicKeyException.class);
    expectedException.expectMessage(
        "AN ERROR MESSAGE");

    algorandASCPollRepository.save(poll);
  }

  @Test
  public void whenSomethingGoesWrongEcondingSignedTransaction()
      throws GeneralSecurityException, JsonProcessingException {

    Poll poll = poll();
    Transaction unsignedTx = new Transaction();

    context.checking(new Expectations() {
      {
        oneOf(unsignedASCTransactionService).createUnsignedTxFor(poll);
        will(returnValue(unsignedTx));

        oneOf(accountCreatorService).createAccountFrom(A_MNEMONIC_KEY);
        will(returnValue(account));

        oneOf(transactionSignerService).sign(unsignedTx, account);
        will(throwException(new JsonMappingException("AN ERROR MESSAGE")));
      }
    });

    expectedException.expect(EncodeTransactionException.class);
    expectedException.expectMessage(
        "Impossible to encode the transaction. AN ERROR MESSAGE");

    algorandASCPollRepository.save(poll);
  }

  @Test
  public void whenSomethingGoesWrongSendingSignedTransaction()
      throws GeneralSecurityException, JsonProcessingException {

    Poll poll = poll();
    Transaction unsignedTx = new Transaction();

    context.checking(new Expectations() {
      {
        oneOf(unsignedASCTransactionService).createUnsignedTxFor(poll);
        will(returnValue(unsignedTx));

        oneOf(accountCreatorService).createAccountFrom(A_MNEMONIC_KEY);
        will(returnValue(account));

        oneOf(transactionSignerService).sign(unsignedTx, account);
        will(throwException(new RuntimeException("AN ERROR MESSAGE")));
      }
    });

    expectedException.expect(SendingTransactionException.class);
    expectedException.expectMessage(
        "Impossible to sign and send the transaction. AN ERROR MESSAGE");

    algorandASCPollRepository.save(poll);
  }

  private Poll poll() {

    return new Poll(A_POLL_NAME, LocalDateTime.now(), LocalDateTime.now(), LocalDateTime.now(),
        LocalDateTime.now(), Collections.emptyList(), "", A_MNEMONIC_KEY, "");
  }
}