package com.pollalgorand.rest.adapter.repository;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.junit.Assert.assertTrue;
import static org.junit.rules.ExpectedException.none;

import com.algorand.algosdk.account.Account;
import com.algorand.algosdk.crypto.Address;
import com.algorand.algosdk.v2.client.common.AlgodClient;
import com.algorand.algosdk.v2.client.common.IndexerClient;
import com.pollalgorand.rest.domain.OptinAppRequest;
import java.security.GeneralSecurityException;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class AlgorandReadRepositoryIT {

  private AlgodClient algodClient;
  IndexerClient indexerClient;
  private AlgorandReadRepository algorandReadRepository;

  @Rule
  public final ExpectedException expectedException = none();

  @Before
  public void setUp() {
    indexerClient = new IndexerClient("https://testnet-algorand.api.purestake.io/idx2", 443, "");
    algorandReadRepository = new AlgorandReadRepository(indexerClient);
  }

  @Test
  public void findAddressSubscribedToApplicationByAppId() throws NoSuchAlgorithmException {

    Address address = new Address(
        "IQ4BYINIYGI7GWGBWGQARY7TYARPF25B262W7LVP6SLTIF3PGYWIS2ECCM");
    Address address1 = new Address(
        "Q4LQ3VZT2H5YE6RPGXJVHAY32KXBWT527VVTUF75UVSYLMDARDEUNPIN5Y");

    List<Address> expectedListOfAddress = algorandReadRepository
        .findAddressSubscribedToApplicationBy("13323770");

    assertThat(expectedListOfAddress, containsInAnyOrder(address, address1));
  }

  @Test
  public void isAccountSubscribedTo() throws GeneralSecurityException {

    assertTrue(
        algorandReadRepository.isAccountSubscribedTo(new OptinAppRequest(13323770L, new Account(
            "share gentle refuse logic shield drift earth initial must match aware they perfect chair say jar harvest echo symbol cave ring void prepare above adult"))));
  }
}