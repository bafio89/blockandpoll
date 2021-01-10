package com.pollalgorand.rest.adapter.repository;

import static java.util.Arrays.asList;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertTrue;
import static org.junit.rules.ExpectedException.none;

import com.algorand.algosdk.account.Account;
import com.algorand.algosdk.crypto.Address;
import com.algorand.algosdk.v2.client.common.IndexerClient;
import com.pollalgorand.rest.adapter.exceptions.ApplicationNotFoundException;
import com.pollalgorand.rest.domain.ApplicationInfoFromBlockchain;
import com.pollalgorand.rest.domain.model.BlockchainPoll;
import com.pollalgorand.rest.domain.request.OptinAppRequest;
import java.math.BigInteger;
import java.security.GeneralSecurityException;
import java.time.LocalDateTime;
import java.util.HashMap;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class AlgorandReadRepositoryIT {

  public static final int API_TIME_DELAY = 2000;
  public static final long NOT_EXISTENT_APP_ID = 123L;
  IndexerClient indexerClient;
  private AlgorandReadRepository algorandReadRepository;

  @Rule
  public final ExpectedException expectedException = none();
  private Address address;
  private Address address1;

  @Before
  public void setUp() throws Exception {
    indexerClient = new IndexerClient("https://testnet-algorand.api.purestake.io/idx2", 443, "");
    algorandReadRepository = new AlgorandReadRepository(indexerClient);
    address = new Address(
        "IQ4BYINIYGI7GWGBWGQARY7TYARPF25B262W7LVP6SLTIF3PGYWIS2ECCM");
    address1 = new Address(
        "Q4LQ3VZT2H5YE6RPGXJVHAY32KXBWT527VVTUF75UVSYLMDARDEUNPIN5Y");
    Thread.sleep(API_TIME_DELAY);
  }

  @Test
  public void isAccountSubscribedTo() throws GeneralSecurityException {

    assertTrue(
        algorandReadRepository.isAccountSubscribedTo(new OptinAppRequest(13323770L, new Account(
            "share gentle refuse logic shield drift earth initial must match aware they perfect chair say jar harvest echo symbol cave ring void prepare above adult"))));
  }

  @Test
  public void hasUserAlreadyVoted() {
    assertTrue(algorandReadRepository.hasAddressAlreadyVotedFor(13334224L,  address));
  }

  @Test
  public void findApplicationInfo() {
    HashMap<String, BigInteger> optionsVotes = new HashMap<>();
    optionsVotes.put("prosciutto", BigInteger.valueOf(3));
    optionsVotes.put("melone", BigInteger.valueOf(1));

    ApplicationInfoFromBlockchain expectedApplicationInfo = new ApplicationInfoFromBlockchain(optionsVotes, 4);
    assertThat(algorandReadRepository.findApplicationInfoBy(aPoll(13334224L)), is(expectedApplicationInfo));
  }

  @Test
  public void whenRetrieveApplicationIsNotFound() {

    expectedException.expect(ApplicationNotFoundException.class);
    expectedException.expectMessage("Impossible to find application with id: 123");

    algorandReadRepository.findApplicationInfoBy(aPoll(NOT_EXISTENT_APP_ID));
  }

  private BlockchainPoll aPoll(long appId) {
    return new BlockchainPoll(appId, "", "", LocalDateTime.now(), LocalDateTime.now(),
        LocalDateTime.now(), LocalDateTime.now(), asList("melone", "prosciutto"), "", "");
  }
}