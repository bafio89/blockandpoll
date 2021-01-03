package com.pollalgorand.rest.adapter.repository;

import static com.pollalgorand.rest.adapter.AlgorandUtils.headers;
import static com.pollalgorand.rest.adapter.AlgorandUtils.values;
import static java.nio.charset.StandardCharsets.UTF_8;
import static java.util.stream.Collectors.toMap;

import com.algorand.algosdk.crypto.Address;
import com.algorand.algosdk.v2.client.common.IndexerClient;
import com.algorand.algosdk.v2.client.common.Response;
import com.algorand.algosdk.v2.client.model.Account;
import com.algorand.algosdk.v2.client.model.AccountResponse;
import com.algorand.algosdk.v2.client.model.AccountsResponse;
import com.algorand.algosdk.v2.client.model.ApplicationResponse;
import com.algorand.algosdk.v2.client.model.TealKeyValue;
import com.pollalgorand.rest.adapter.exceptions.AlgorandInteractionError;
import com.pollalgorand.rest.domain.ApplicationInfoFromBlockchain;
import com.pollalgorand.rest.domain.model.BlockchainPoll;
import com.pollalgorand.rest.domain.repository.BlockchainReadRepository;
import com.pollalgorand.rest.domain.request.OptinAppRequest;
import java.math.BigInteger;
import java.util.Base64;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AlgorandReadRepository implements BlockchainReadRepository {

  private Logger logger = LoggerFactory.getLogger(AlgorandReadRepository.class);

  public static final String VOTED_REPRESENTATION = Base64.getEncoder()
      .encodeToString("voted".getBytes(UTF_8));

  private final IndexerClient indexerClient;

  public AlgorandReadRepository(IndexerClient indexerClient) {

    this.indexerClient = indexerClient;
  }

  @Override
  public Boolean isAccountSubscribedTo(OptinAppRequest optinAppRequest) {

    Response<AccountResponse> response;

    try {
      response = indexerClient
          .lookupAccountByID(optinAppRequest.getAccount().getAddress()).execute(headers, values);
    } catch (Exception e) {
      throw new AlgorandInteractionError(e.getMessage());
    }

    if (response.code() == 200) {
      return response.body().account.appsLocalState.stream()
          .anyMatch(app -> app.id == optinAppRequest.getAppId());
    } else {
      logger.error(
          "An error occurs calling algorand blockchain in order to retrieve account subscripted. Response code {}. Message {}. AppId {}. Address {}",
          response.code(), response.message(), optinAppRequest.getAppId(),
          optinAppRequest.getAccount().getAddress().toString());
      throw new AlgorandInteractionError(response.code(), response.message());
    }
  }

  public Boolean hasAddressAlreadyVotedFor(long appId, Address address) {
    Response<AccountResponse> response;
    try {

      response = indexerClient.lookupAccountByID(address)
          .execute(headers, values);
    } catch (Exception e) {
      throw new AlgorandInteractionError(e.getMessage());
    }

    if (response.code() == 200) {
      return response.body().account.appsLocalState.stream().filter(app -> app.id.equals(appId))
          .anyMatch(app -> app.keyValue.stream()
              .anyMatch(tealKeyValue -> tealKeyValue.key.equals(VOTED_REPRESENTATION)));
    }

    throw new AlgorandInteractionError(response.code(), response.message());
  }

  @Override
  public ApplicationInfoFromBlockchain findApplicationInfoBy(BlockchainPoll poll) {
    Map<String, BigInteger> optionsVotes = findOptionsVotes(poll);
    try {
      Thread.sleep(2000);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    List<Address> subscribedAddress = findAddressSubscribedToApplicationBy(poll.getAppId());
    return new ApplicationInfoFromBlockchain(optionsVotes, subscribedAddress.size());
  }

  private List<Address> findAddressSubscribedToApplicationBy(long appId) {

    Response<AccountsResponse> response;

    try {
      response = indexerClient.searchForAccounts()
          .applicationId(appId).execute(headers, values);
    } catch (Exception e) {
      throw new RuntimeException();
    }

    if (response.code() == 200) {
      List<Account> accounts = response.body().accounts;
      return accounts.stream().map(account -> account.address).collect(Collectors.toList());
    }
    logger.error(
        "An error occurs calling algorand blockchain finding address subscribed to an app. Response code {}. Message {}. AppId {}.",
        response.code(), response.message(), appId);
    throw new AlgorandInteractionError(response.code(), response.message());

  }

  private Map<String, BigInteger> findOptionsVotes(BlockchainPoll poll) {
    Response<ApplicationResponse> response = null;
    try {
      response = indexerClient.lookupApplicationByID(poll.getAppId()).execute(headers, values);
    } catch (Exception e) {
      e.printStackTrace();
    }

    if (response.code() == 200) {

      return response.body().application.params.globalState.stream()
          .filter(keysRepresentingOption(poll.getOptions()))
          .collect(toMap((tealKeyValue -> decodeString(tealKeyValue.key)),
              tealKeyValue -> tealKeyValue.value.uint));
    }
    logger.error(
        "An error occurs calling algorand blockchain during application info retrieving. Response code {}. Message {}. AppId {}.",
        response.code(), response.message(), poll.getAppId());
    throw new AlgorandInteractionError(response.code(), response.message());
  }

  private Predicate<TealKeyValue> keysRepresentingOption(List<String> options) {
    return tealKeyValue -> options.contains(decodeString(tealKeyValue.key));
  }

  private String decodeString(String tealKey) {
    return new String(Base64.getDecoder().decode(tealKey));
  }
}