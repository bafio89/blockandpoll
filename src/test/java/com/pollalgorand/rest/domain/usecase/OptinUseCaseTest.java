package com.pollalgorand.rest.domain.usecase;

import com.algorand.algosdk.account.Account;
import com.pollalgorand.rest.domain.DateValidator;
import com.pollalgorand.rest.domain.OptinAppRequest;
import com.pollalgorand.rest.domain.exceptions.OptinAlreadyDoneException;
import com.pollalgorand.rest.domain.model.BlockchainPoll;
import com.pollalgorand.rest.domain.repository.BlockchainReadRepository;
import com.pollalgorand.rest.domain.repository.BlockchainWriteRepository;
import com.pollalgorand.rest.domain.repository.PollRepository;
import java.security.NoSuchAlgorithmException;
import java.util.Optional;
import org.jmock.Expectations;
import org.jmock.auto.Mock;
import org.jmock.integration.junit4.JUnitRuleMockery;
import org.jmock.lib.legacy.ClassImposteriser;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class OptinUseCaseTest {

  public static final long APP_ID = 123L;
  @Rule
  public JUnitRuleMockery context = new JUnitRuleMockery() {{
    setImposteriser(ClassImposteriser.INSTANCE);
  }};

  @Rule
  public ExpectedException expectedException = ExpectedException.none();

  @Mock
  private BlockchainReadRepository blockChainReadRepository;

  @Mock
  private BlockchainWriteRepository blockchainWriteRepository;

  @Mock
  private PollRepository pollRepository;

  @Mock
  private DateValidator dateValidator;

  private OptinUseCase optinUseCase;

  private BlockchainPoll blockchainPoll = new BlockchainPoll();

  public static OptinAppRequest optinAppRequest;

  @Before
  public void setUp() throws NoSuchAlgorithmException {
    optinUseCase = new OptinUseCase(blockChainReadRepository, blockchainWriteRepository,
        pollRepository, dateValidator);
    optinAppRequest = new OptinAppRequest(APP_ID, new Account());
  }

  @Test
  public void optinOk() {

    context.checking(new Expectations() {{
      oneOf(pollRepository).findBy(APP_ID);
      will(returnValue(Optional.of(blockchainPoll)));

      oneOf(dateValidator).isNowInInterval(blockchainPoll.getStartSubscriptionTime(), blockchainPoll.getEndSubscriptionTime());
      will(returnValue(Boolean.TRUE));

      oneOf(blockChainReadRepository).isAccountSubscribedTo(optinAppRequest);
      will(returnValue(Boolean.FALSE));

      oneOf(blockchainWriteRepository).optin(optinAppRequest);
    }});

    optinUseCase.optin(optinAppRequest);
  }

  @Test
  public void whenOptinIsExpired() {

    context.checking(new Expectations() {{
      oneOf(pollRepository).findBy(APP_ID);
      will(returnValue(Optional.of(blockchainPoll)));

      oneOf(dateValidator).isNowInInterval(blockchainPoll.getStartSubscriptionTime(), blockchainPoll.getEndSubscriptionTime());
      will(returnValue(Boolean.FALSE));

      never(blockChainReadRepository);

      never(blockchainWriteRepository);
    }});

    expectedException.expect(OptinIntervalTimeException.class);
    expectedException.expectMessage("Optin app 123 is not open or expired");


    optinUseCase.optin(optinAppRequest);
  }

  @Test
  public void whenUserAlreadyOptedIn() {

    context.checking(new Expectations() {{
      oneOf(pollRepository).findBy(APP_ID);
      will(returnValue(Optional.of(blockchainPoll)));

      oneOf(dateValidator).isNowInInterval(blockchainPoll.getStartSubscriptionTime(), blockchainPoll.getEndSubscriptionTime());
      will(returnValue(Boolean.TRUE));

      oneOf(blockChainReadRepository).isAccountSubscribedTo(optinAppRequest);
      will(returnValue(Boolean.TRUE));

      never(blockchainWriteRepository);
    }});

    expectedException.expect(OptinAlreadyDoneException.class);
    expectedException
        .expectMessage("It seems that optin has been already done for app 123");

    optinUseCase.optin(optinAppRequest);
  }
}