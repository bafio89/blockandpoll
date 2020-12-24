package com.pollalgorand.rest.domain.usecase;

import com.pollalgorand.rest.domain.DateValidator;
import com.pollalgorand.rest.domain.OptinAppRequest;
import com.pollalgorand.rest.domain.exceptions.OptinAlreadDoneException;
import com.pollalgorand.rest.domain.model.BlockchainPoll;
import com.pollalgorand.rest.domain.repository.BlockchainReadRepository;
import com.pollalgorand.rest.domain.repository.BlockchainWriteRepository;
import com.pollalgorand.rest.domain.repository.PollRepository;
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

  public static final OptinAppRequest optinAppRequest = new OptinAppRequest(APP_ID, "SENDER", "MNEMONIC_KEY");

  @Before
  public void setUp() {
    optinUseCase = new OptinUseCase(blockChainReadRepository, blockchainWriteRepository,
        pollRepository, dateValidator);
  }

  @Test
  public void optinOk() {

    context.checking(new Expectations() {{
      oneOf(pollRepository).findBy(APP_ID);
      will(returnValue(blockchainPoll));

      oneOf(dateValidator).isOptinExpired(blockchainPoll);
      will(returnValue(Boolean.FALSE));

      oneOf(blockChainReadRepository).isOptinAllowedFor(optinAppRequest);
      will(returnValue(Boolean.TRUE));

      oneOf(blockchainWriteRepository).optin(optinAppRequest);
    }});

    optinUseCase.optin(optinAppRequest);
  }

  @Test
  public void whenOptinIsExpired() {

    context.checking(new Expectations() {{
      oneOf(pollRepository).findBy(APP_ID);
      will(returnValue(blockchainPoll));

      oneOf(dateValidator).isOptinExpired(blockchainPoll);
      will(returnValue(Boolean.TRUE));

      never(blockChainReadRepository);

      never(blockchainWriteRepository);
    }});

    expectedException.expect(OptinExpiredException.class);
    expectedException.expectMessage("Optin app 123 is expired");


    optinUseCase.optin(optinAppRequest);
  }

  @Test
  public void whenUserAlreadyOptedIn() {

    context.checking(new Expectations() {{
      oneOf(pollRepository).findBy(APP_ID);
      will(returnValue(blockchainPoll));

      oneOf(dateValidator).isOptinExpired(blockchainPoll);
      will(returnValue(Boolean.FALSE));

      oneOf(blockChainReadRepository).isOptinAllowedFor(optinAppRequest);
      will(returnValue(Boolean.FALSE));

      never(blockchainWriteRepository);
    }});

    expectedException.expect(OptinAlreadDoneException.class);
    expectedException
        .expectMessage("It seems that optin has been already done for app 123 and sender SENDER");

    optinUseCase.optin(optinAppRequest);
  }
}