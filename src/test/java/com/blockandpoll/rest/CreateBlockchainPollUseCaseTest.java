package com.blockandpoll.rest;

import static java.util.Arrays.asList;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import com.algorand.algosdk.account.Account;
import com.blockandpoll.rest.adapter.exceptions.SavingToDbException;
import com.blockandpoll.rest.adapter.service.AccountCreatorService;
import com.blockandpoll.rest.adapter.service.UnsignedASCTransactionService;
import com.blockandpoll.rest.domain.model.BlockchainPoll;
import com.blockandpoll.rest.domain.model.Poll;
import com.blockandpoll.rest.domain.repository.BlockchainPollRepository;
import com.blockandpoll.rest.domain.repository.PollRepository;
import com.blockandpoll.rest.domain.usecase.CreatePollUseCase;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import org.jmock.Expectations;
import org.jmock.auto.Mock;
import org.jmock.integration.junit4.JUnitRuleMockery;
import org.jmock.lib.legacy.ClassImposteriser;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class CreateBlockchainPollUseCaseTest {

  public static final LocalDateTime DATE = LocalDateTime.now();
  public static final String MNEMONIC_KEY = "mnemonicKey";
  public static final long APP_ID = 123L;
  public static final String A_POLL_NAME1 = "A POLL NAME";
  public static final String A_POLL_NAME = "A POLL NAME";
  public static final String A_DESCRIPTION = "A DESCRIPTION";
  public static final String OPTION_1 = "OPTION 1";
  public static final String OPTION_2 = "OPTION 2";
  public static final String A_QUESTION = "A QUESTION";
  @Rule
  public JUnitRuleMockery context = new JUnitRuleMockery() {{
    setImposteriser(ClassImposteriser.INSTANCE);
  }};

  public static final LocalDateTime START_VOTING_TIME = LocalDateTime.of(2020, 12, 31, 0, 0);
  public static final LocalDateTime END_VOTING_TIME = LocalDateTime.of(2021, 1, 3, 0, 0);
  public static final LocalDateTime START_SUBSCRIPTION_TIME = LocalDateTime.of(2020, 12, 20, 0, 0);
  public static final LocalDateTime END_SUBSCRIPTION_TIME = LocalDateTime.of(2020, 12, 30, 0, 0);


  @Rule
  public ExpectedException expectedException = ExpectedException.none();

  @Mock
  private BlockchainPollRepository blockChainPollRepository;

  @Mock
  private PollRepository postgresRepository;

  @Mock
  private UnsignedASCTransactionService unsignedASCTransactionService;

  @Mock
  private AccountCreatorService accountCreatorService;

  private CreatePollUseCase createPollUseCase;

  @Before
  public void setUp() {
    createPollUseCase = new CreatePollUseCase(blockChainPollRepository, postgresRepository,
        unsignedASCTransactionService, accountCreatorService);
  }

  @Test
  public void happyPath() {

    Poll createPollRequest = aPoll();

    BlockchainPoll expectedBlockchainPoll = aBlockChainPoll();

    context.checking(new Expectations() {{

      oneOf(blockChainPollRepository).save(createPollRequest);
      will(returnValue(expectedBlockchainPoll));

      oneOf(postgresRepository).save(expectedBlockchainPoll);
    }});

    Poll poll = createPollUseCase.create(createPollRequest);

    assertThat(poll, is(expectedBlockchainPoll));
  }


  @Test
  public void whenBlockchainPollCreationFails() {

    Poll createPollRequest = aPoll();

    context.checking(new Expectations() {{

      oneOf(blockChainPollRepository).save(createPollRequest);
      will(throwException(new RuntimeException()));

      never(postgresRepository);
    }});

    expectedException.expect(RuntimeException.class);

    createPollUseCase.create(createPollRequest);
  }

    @Test
  public void whenPostgresRepositorySavingFails() {

    Poll poll = aPoll();

    BlockchainPoll blockchainPollGeneratedFromBlockchain = aBlockChainPoll();

    context.checking(new Expectations() {{

      oneOf(blockChainPollRepository).save(poll);
      will(returnValue(blockchainPollGeneratedFromBlockchain));

      oneOf(postgresRepository).save(blockchainPollGeneratedFromBlockchain);
      will(throwException(new SavingToDbException(A_POLL_NAME, "AN ERROR")));
    }});

    expectedException.expect(SavingToDbException.class);
    expectedException.expectMessage("An error occours trying to save the poll with name:A POLL NAME in the DB. Error message: AN ERROR.");

    createPollUseCase.create(poll);

  }
  @Test
  public void createTransactionHappyPath() throws NoSuchAlgorithmException {
    Poll poll = aPoll();

    Account account = new Account();

    context.checking(new Expectations() {{
      oneOf(accountCreatorService).createAccountFrom(MNEMONIC_KEY);
      will(returnValue(account));

      oneOf(unsignedASCTransactionService).createUnsignedTxFor(poll, account);
    }});

    createPollUseCase.createUnsignedTx(poll);
  }

  private Poll aPoll() {
    return new Poll(A_POLL_NAME, DATE, DATE, DATE,
        DATE, asList(OPTION_1, OPTION_2), A_QUESTION, MNEMONIC_KEY, A_DESCRIPTION);
  }

  private BlockchainPoll aBlockChainPoll() {
    return new BlockchainPoll(APP_ID, A_POLL_NAME, A_QUESTION,
        DATE,
        DATE,
        DATE,
        DATE,
        asList(OPTION_1, OPTION_2), MNEMONIC_KEY, A_DESCRIPTION);
  }
}