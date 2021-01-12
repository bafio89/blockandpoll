package com.blockandpoll.rest.adapter.repository;

import com.algorand.algosdk.account.Account;
import com.algorand.algosdk.transaction.Transaction;
import com.algorand.algosdk.v2.client.common.AlgodClient;
import com.blockandpoll.rest.adapter.service.BuildOptinTransactionService;
import com.blockandpoll.rest.adapter.service.BuildVoteTransactionService;
import com.blockandpoll.rest.adapter.service.TransactionWriterService;
import com.blockandpoll.rest.domain.repository.BlockchainWriteRepository;
import com.blockandpoll.rest.domain.request.OptinAppRequest;
import org.jmock.Expectations;
import org.jmock.auto.Mock;
import org.jmock.integration.junit4.JUnitRuleMockery;
import org.jmock.lib.legacy.ClassImposteriser;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class AlgorandWriteRepositoryOptinTest {

  @Rule
  public JUnitRuleMockery context = new JUnitRuleMockery() {{
    setImposteriser(ClassImposteriser.INSTANCE);
  }};

  @Rule
  public ExpectedException expectedException = ExpectedException.none();

  @Mock
  private AlgodClient algodClient;

  @Mock
  private BuildOptinTransactionService buildOptinTransactionService;

  @Mock
  private TransactionWriterService transactionWriterService;

  @Mock
  private BuildVoteTransactionService buildVoteTransactionService;

  private BlockchainWriteRepository blockchainWriteRepository;

  @Before
  public void setUp() {
    blockchainWriteRepository = new AlgorandWriteRepository(
        buildOptinTransactionService, buildVoteTransactionService,
        transactionWriterService);
  }

  @Test
  public void optinOk() throws Exception {

    Account account = new Account();
    OptinAppRequest optinAppRequest = new OptinAppRequest(123L, account);
    Transaction unsignedTx = new Transaction();

    context.checking(new Expectations() {{
      oneOf(buildOptinTransactionService).buildTransaction(optinAppRequest);
      will(returnValue(unsignedTx));

      oneOf(transactionWriterService).write(account, unsignedTx);

    }});

    blockchainWriteRepository.optin(optinAppRequest);
  }
}