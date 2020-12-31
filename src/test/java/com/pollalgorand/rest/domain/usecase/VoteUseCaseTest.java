package com.pollalgorand.rest.domain.usecase;

import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;
import static java.util.Arrays.asList;
import static org.junit.rules.ExpectedException.none;

import com.algorand.algosdk.account.Account;
import com.pollalgorand.rest.domain.DateValidator;
import com.pollalgorand.rest.domain.exceptions.AlreadyVotedException;
import com.pollalgorand.rest.domain.exceptions.PollNotFoundException;
import com.pollalgorand.rest.domain.model.BlockchainPoll;
import com.pollalgorand.rest.domain.repository.BlockchainReadRepository;
import com.pollalgorand.rest.domain.repository.BlockchainWriteRepository;
import com.pollalgorand.rest.domain.repository.PollRepository;
import com.pollalgorand.rest.domain.request.OptinAppRequest;
import com.pollalgorand.rest.domain.request.VoteAppRequest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.util.Optional;
import org.jmock.Expectations;
import org.jmock.auto.Mock;
import org.jmock.integration.junit4.JUnitRuleMockery;
import org.jmock.lib.legacy.ClassImposteriser;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class VoteUseCaseTest {

  public static final String AN_OPTION = "AN OPTION";
  public static final long APP_ID = 123L;
  public static final LocalDateTime START_VOTING_TIME = LocalDateTime.of(2020, 12, 31, 0, 0);
  public static final LocalDateTime END_VOTING_TIME = LocalDateTime.of(2021, 1, 3, 0, 0);
  public static final LocalDateTime START_SUBSCRIPTION_TIME = LocalDateTime.of(2020, 12, 20, 0, 0);
  public static final LocalDateTime END_SUBSCRIPTION_TIME = LocalDateTime.of(2020, 12, 30, 0, 0);

  @Rule
  public JUnitRuleMockery context = new JUnitRuleMockery() {{
    setImposteriser(ClassImposteriser.INSTANCE);
  }};

  @Rule
  public final ExpectedException expectedException = none();


  @Mock
  private PollRepository pollRepository;

  @Mock
  private BlockchainReadRepository algorandReadRepository;

  @Mock
  private DateValidator dateValidator;

  @Mock
  private VoteUseCase voteUseCase;

  @Mock
  private BlockchainWriteRepository blockchainWriteRepository;

  @Before
  public void setUp() {
    voteUseCase = new VoteUseCase(pollRepository, dateValidator, algorandReadRepository,
        blockchainWriteRepository);
  }

  @Test
  public void whenVoteIsOpenAndOptinHasBeenDone() throws NoSuchAlgorithmException {

    BlockchainPoll blockchainPoll = aBlockchainPoll();
    Account account = new Account();
    VoteAppRequest voteAppRequest = new VoteAppRequest(APP_ID, account, AN_OPTION);

    context.checking(new Expectations() {{
      oneOf(pollRepository).findBy(APP_ID);
      will(returnValue(Optional.of(blockchainPoll)));

      oneOf(dateValidator).isNowInInterval(START_VOTING_TIME, END_VOTING_TIME);
      will(returnValue(TRUE));

      oneOf(algorandReadRepository).hasAddressAlreadyVotedFor(APP_ID, account.getAddress());
      will(returnValue(FALSE));

      oneOf(algorandReadRepository).isAccountSubscribedTo(new OptinAppRequest(APP_ID, account));
      will(returnValue(TRUE));

      oneOf(blockchainWriteRepository).vote(voteAppRequest);
    }});

    voteUseCase.vote(voteAppRequest);
  }

  @Test
  public void whenVoteIsOpenAndOptinIsOpenAndNOTBeenDone() throws NoSuchAlgorithmException {

    BlockchainPoll blockchainPoll = aBlockchainPoll();
    Account account = new Account();
    OptinAppRequest optinAppRequest = new OptinAppRequest(APP_ID, account);
    VoteAppRequest voteAppRequest = new VoteAppRequest(APP_ID, account, AN_OPTION);

    context.checking(new Expectations() {{
      oneOf(pollRepository).findBy(APP_ID);
      will(returnValue(Optional.of(blockchainPoll)));

      oneOf(dateValidator).isNowInInterval(START_VOTING_TIME, END_VOTING_TIME);
      will(returnValue(TRUE));

      oneOf(algorandReadRepository).hasAddressAlreadyVotedFor(APP_ID, account.getAddress());
      will(returnValue(FALSE));

      oneOf(algorandReadRepository).isAccountSubscribedTo(optinAppRequest);
      will(returnValue(FALSE));

      oneOf(dateValidator).isNowInInterval(START_SUBSCRIPTION_TIME, END_SUBSCRIPTION_TIME);
      will(returnValue(TRUE));

      oneOf(blockchainWriteRepository).optin(optinAppRequest);
      oneOf(blockchainWriteRepository).vote(voteAppRequest);
    }});

    voteUseCase.vote(voteAppRequest);
  }

  @Test
  public void whenAddressHasAlreadyVoted() throws NoSuchAlgorithmException {
    BlockchainPoll blockchainPoll = aBlockchainPoll();
    Account account = new Account();
    OptinAppRequest optinAppRequest = new OptinAppRequest(APP_ID, account);
    VoteAppRequest voteAppRequest = new VoteAppRequest(APP_ID, account, AN_OPTION);

    context.checking(new Expectations() {{
      oneOf(pollRepository).findBy(APP_ID);
      will(returnValue(Optional.of(blockchainPoll)));

      oneOf(dateValidator).isNowInInterval(START_VOTING_TIME, END_VOTING_TIME);
      will(returnValue(TRUE));

      oneOf(algorandReadRepository).hasAddressAlreadyVotedFor(APP_ID, account.getAddress());
      will(returnValue(TRUE));

      never(algorandReadRepository).isAccountSubscribedTo(optinAppRequest);

      never(dateValidator).isNowInInterval(START_SUBSCRIPTION_TIME, END_SUBSCRIPTION_TIME);

      never(blockchainWriteRepository);
    }});

    expectedException.expect(AlreadyVotedException.class);
    expectedException.expectMessage(String
        .format("Address %s have already voted for appId %s", account.getAddress().toString(),
            APP_ID));

    voteUseCase.vote(voteAppRequest);
  }

  @Test
  public void whenOptinHasNotBeenDoneAndSubscriptionTimeIsOpen() throws NoSuchAlgorithmException {
    BlockchainPoll blockchainPoll = aBlockchainPoll();
    Account account = new Account();
    OptinAppRequest optinAppRequest = new OptinAppRequest(APP_ID, account);
    VoteAppRequest voteAppRequest = new VoteAppRequest(APP_ID, account, AN_OPTION);

    context.checking(new Expectations() {{
      oneOf(pollRepository).findBy(APP_ID);
      will(returnValue(Optional.of(blockchainPoll)));

      oneOf(dateValidator).isNowInInterval(START_VOTING_TIME, END_VOTING_TIME);
      will(returnValue(TRUE));

      oneOf(algorandReadRepository).hasAddressAlreadyVotedFor(APP_ID, account.getAddress());
      will(returnValue(FALSE));

      oneOf(algorandReadRepository).isAccountSubscribedTo(optinAppRequest);
      will(returnValue(FALSE));

      oneOf(dateValidator).isNowInInterval(START_SUBSCRIPTION_TIME, END_SUBSCRIPTION_TIME);
      will(returnValue(FALSE));

      never(blockchainWriteRepository);
    }});

    expectedException.expect(OptinIntervalTimeException.class);
    expectedException.expectMessage(String.format("Optin app %s is not open or expired", APP_ID));

    voteUseCase.vote(voteAppRequest);
  }

  @Test
  public void whenPollIsNotFound() throws NoSuchAlgorithmException {
    Account account = new Account();
    VoteAppRequest voteAppRequest = new VoteAppRequest(APP_ID, account, AN_OPTION);

    context.checking(new Expectations() {{
      oneOf(pollRepository).findBy(APP_ID);
      will(returnValue(Optional.empty()));

      never(dateValidator);
      never(algorandReadRepository);
      never(blockchainWriteRepository);
    }});

    expectedException.expect(PollNotFoundException.class);
    expectedException
        .expectMessage(String.format("Impossible to found the poll with appId: %s", APP_ID));

    voteUseCase.vote(voteAppRequest);
  }

  private BlockchainPoll aBlockchainPoll() {
    return new BlockchainPoll(APP_ID, "A POLL NAME", "A SENDER",
        START_SUBSCRIPTION_TIME,
        END_SUBSCRIPTION_TIME,
        START_VOTING_TIME,
        END_VOTING_TIME,
        asList("OPTION 1", "OPTION 2"), "A MNEMONIC KEY", "A DESCRIPTION");
  }
}