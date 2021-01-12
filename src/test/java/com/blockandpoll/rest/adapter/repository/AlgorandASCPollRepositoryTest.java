package com.blockandpoll.rest.adapter.repository;

import static org.junit.rules.ExpectedException.none;

import com.algorand.algosdk.account.Account;
import com.algorand.algosdk.transaction.Transaction;
import com.algorand.algosdk.v2.client.common.AlgodClient;
import com.algorand.algosdk.v2.client.common.Response;
import com.algorand.algosdk.v2.client.model.PostTransactionsResponse;
import com.blockandpoll.rest.adapter.converter.PollBlockchainAdapter;
import com.blockandpoll.rest.adapter.exceptions.InvalidMnemonicKeyException;
import com.blockandpoll.rest.adapter.service.AccountCreatorService;
import com.blockandpoll.rest.adapter.service.AlgorandApplicationService;
import com.blockandpoll.rest.adapter.service.TransactionWriterService;
import com.blockandpoll.rest.adapter.service.UnsignedASCTransactionService;
import com.blockandpoll.rest.domain.model.Poll;
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

  @Rule
  public JUnitRuleMockery context = new JUnitRuleMockery() {{
    setImposteriser(ClassImposteriser.INSTANCE);
  }};

  @Rule
  public final ExpectedException expectedException = none();

  @Mock
  private AlgodClient algodClient;

  @Mock
  private UnsignedASCTransactionService unsignedASCTransactionService;

  @Mock
  private Response executeResponse;

  @Mock
  private AccountCreatorService accountCreatorService;

  @Mock
  private PollBlockchainAdapter pollBlockchainAdapter;

  @Mock
  private AlgorandApplicationService algorandApplicationService;

  @Mock
  private TransactionWriterService transactionWriterService;

  private AlgorandASCPollRepository algorandASCPollRepository;
  private Account account;

  @Before
  public void setUp() throws NoSuchAlgorithmException {
    algorandASCPollRepository = new AlgorandASCPollRepository(algodClient, accountCreatorService,
        unsignedASCTransactionService, pollBlockchainAdapter,
        algorandApplicationService, transactionWriterService);

    account = new Account();
  }

  @Test
  public void happyPath() {

    Poll poll = poll();
    Transaction unsignedTx = new Transaction();

    PostTransactionsResponse postTransactionResponse = new PostTransactionsResponse();
    postTransactionResponse.txId = TX_ID;

    context.checking(new Expectations() {{
      oneOf(unsignedASCTransactionService).createUnsignedTxFor(poll, account);
      will(returnValue(unsignedTx));

      oneOf(accountCreatorService).createAccountFrom(A_MNEMONIC_KEY);
      will(returnValue(account));

      oneOf(transactionWriterService).write(account, unsignedTx);
      will(returnValue(TX_ID));

      oneOf(algorandApplicationService).getApplicationId(TX_ID);
      will(returnValue(A_TRANSACTION_ID));

      oneOf(pollBlockchainAdapter).fromPollToBlockchainPoll(poll, A_TRANSACTION_ID);
    }});

    algorandASCPollRepository.save(poll);
  }

  @Test
  public void whenThereAreErrorsCreatingAccount() {
    Poll poll = poll();
    Transaction unsignedTx = new Transaction();

    context.checking(new Expectations() {
      {
        oneOf(unsignedASCTransactionService).createUnsignedTxFor(poll, account);
        will(returnValue(unsignedTx));

        oneOf(accountCreatorService).createAccountFrom(A_MNEMONIC_KEY);
        will(throwException(new IllegalArgumentException("AN ERROR MESSAGE")));

        never(transactionWriterService);
      }
    });

    expectedException.expect(InvalidMnemonicKeyException.class);
    expectedException.expectMessage("AN ERROR MESSAGE");

    algorandASCPollRepository.save(poll);
  }

  private Poll poll() {

    return new Poll(A_POLL_NAME, LocalDateTime.now(), LocalDateTime.now(), LocalDateTime.now(),
        LocalDateTime.now(), Collections.emptyList(), "", A_MNEMONIC_KEY, "");
  }
}