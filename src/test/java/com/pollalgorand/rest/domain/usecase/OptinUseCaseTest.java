package com.pollalgorand.rest.domain.usecase;

import com.pollalgorand.rest.domain.OptinAppRequest;
import com.pollalgorand.rest.domain.exceptions.OptinAlreadDoneException;
import com.pollalgorand.rest.domain.repository.BlockchainReadRepository;
import com.pollalgorand.rest.domain.repository.BlockchainWriteRepository;
import org.jmock.Expectations;
import org.jmock.auto.Mock;
import org.jmock.integration.junit4.JUnitRuleMockery;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class OptinUseCaseTest {

  @Rule
  public JUnitRuleMockery context = new JUnitRuleMockery();

  @Rule
  public ExpectedException expectedException = ExpectedException.none();


  @Mock
  private BlockchainReadRepository blockChainReadRepository;

  @Mock
  private BlockchainWriteRepository blockchainWriteRepository;

  private OptinUseCase optinUseCase;

  @Before
  public void setUp() {
    optinUseCase = new OptinUseCase(blockChainReadRepository, blockchainWriteRepository);
  }

  @Test
  public void optinOk() {

    OptinAppRequest optinAppRequest = new OptinAppRequest(123L, "SENDER", "MNEMONIC_KEY");

    context.checking(new Expectations() {{
      oneOf(blockChainReadRepository).isOptinAllowedFor(
          optinAppRequest);
      will(returnValue(Boolean.TRUE));

      oneOf(blockchainWriteRepository).optin(optinAppRequest);
    }});

    optinUseCase.optin(optinAppRequest);
  }

  @Test
  public void whenUserAlreadyOptedIn() {

    OptinAppRequest optinAppRequest = new OptinAppRequest(123L, "SENDER", "MNEMONIC_KEY");

    context.checking(new Expectations() {{
      oneOf(blockChainReadRepository).isOptinAllowedFor(
          optinAppRequest);
      will(returnValue(Boolean.FALSE));

      never(blockchainWriteRepository);
    }});

    expectedException.expect(OptinAlreadDoneException.class);
    expectedException.expectMessage("It seems that optin has been already done for app 123 and sender SENDER");

    optinUseCase.optin(optinAppRequest);
  }
}