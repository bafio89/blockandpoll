package com.pollalgorand.rest.adapter.repository;

import static java.nio.charset.StandardCharsets.UTF_8;

import com.algorand.algosdk.account.Account;
import com.algorand.algosdk.transaction.Transaction;
import com.algorand.algosdk.v2.client.common.AlgodClient;
import com.pollalgorand.rest.adapter.service.BuildOptinTransactionService;
import com.pollalgorand.rest.adapter.service.BuildVoteTransactionService;
import com.pollalgorand.rest.adapter.service.TransactionWriterService;
import com.pollalgorand.rest.domain.repository.BlockchainWriteRepository;
import com.pollalgorand.rest.domain.request.OptinAppRequest;
import org.jmock.Expectations;
import org.jmock.auto.Mock;
import org.jmock.integration.junit4.JUnitRuleMockery;
import org.jmock.lib.legacy.ClassImposteriser;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class AlgorandWriteRepositoryTest {

  public static final byte[] A_BYTE_ARRAY = "A_BYTE_ARRAY".getBytes(UTF_8);
  public static final String TRANSACTION_ID = "666";

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
  public void happyPath() throws Exception {

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