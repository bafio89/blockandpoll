package com.blockandpoll.rest.adapter.repository;

import static java.nio.charset.StandardCharsets.UTF_8;

import com.algorand.algosdk.account.Account;
import com.algorand.algosdk.transaction.Transaction;
import com.algorand.algosdk.v2.client.common.AlgodClient;
import com.blockandpoll.rest.adapter.service.BuildOptinTransactionService;
import com.blockandpoll.rest.adapter.service.BuildVoteTransactionService;
import com.blockandpoll.rest.adapter.service.TransactionConfirmationService;
import com.blockandpoll.rest.adapter.service.TransactionSenderService;
import com.blockandpoll.rest.adapter.service.TransactionSignerService;
import com.blockandpoll.rest.adapter.service.TransactionWriterService;
import com.blockandpoll.rest.domain.repository.BlockchainWriteRepository;
import com.blockandpoll.rest.domain.request.VoteAppRequest;
import java.security.NoSuchAlgorithmException;
import org.jmock.Expectations;
import org.jmock.auto.Mock;
import org.jmock.integration.junit4.JUnitRuleMockery;
import org.jmock.lib.legacy.ClassImposteriser;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class AlgorandWriteRepositoryVoteTest {

  public static final byte[] A_BYTE_ARRAY = "A_BYTE_ARRAY".getBytes(UTF_8);
  public static final String TRANSACTION_ID = "666";
  public static final String AN_OPTION = "AN OPTION";
  public static final long APP_ID = 123L;

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
  private BuildVoteTransactionService buildVoteTransactionService;

  @Mock
  private TransactionSignerService transactionSignerService;

  @Mock
  private TransactionSenderService transactionSenderService;

  @Mock
  private TransactionConfirmationService transactionConfirmationService;

  @Mock
  private TransactionWriterService transactionWriterService;

  private BlockchainWriteRepository blockchainWriteRepository;

  @Before
  public void setUp() {
    blockchainWriteRepository = new AlgorandWriteRepository(
        buildOptinTransactionService, buildVoteTransactionService,
        transactionWriterService);
  }

  @Test
  public void voteOk() throws NoSuchAlgorithmException {

    Account account = new Account();
    Transaction unsignedTransaction = new Transaction();
    VoteAppRequest voteAppRequest = new VoteAppRequest(APP_ID, account, AN_OPTION);

    context.checking(new Expectations(){{
      oneOf(buildVoteTransactionService).buildTransaction(voteAppRequest);
      will(returnValue(unsignedTransaction));

      oneOf(transactionWriterService).write(account, unsignedTransaction);
    }});

    blockchainWriteRepository.vote(voteAppRequest);
  }
}