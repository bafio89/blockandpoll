package com.pollalgorand.rest.adapter.repository;

import static com.pollalgorand.rest.adapter.AlgorandUtils.headers;
import static com.pollalgorand.rest.adapter.AlgorandUtils.values;

import com.algorand.algosdk.crypto.Address;
import com.algorand.algosdk.v2.client.common.IndexerClient;
import com.algorand.algosdk.v2.client.common.Response;
import com.algorand.algosdk.v2.client.model.Account;
import com.algorand.algosdk.v2.client.model.AccountResponse;
import com.pollalgorand.rest.adapter.exceptions.AlgorandInteractionError;
import com.pollalgorand.rest.domain.OptinAppRequest;
import com.pollalgorand.rest.domain.repository.BlockchainReadRepository;
import java.util.List;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AlgorandReadRepository implements BlockchainReadRepository {

  private Logger logger = LoggerFactory.getLogger(AlgorandReadRepository.class);

  private IndexerClient indexerClient;

  public AlgorandReadRepository(IndexerClient indexerClient) {

    this.indexerClient = indexerClient;
  }

  @Override
  public List<Address> findAddressSubscribedToApplicationBy(String appId) {
    try {
      List<Account> accounts = indexerClient.searchForAccounts()
          .applicationId(Long.valueOf(appId)).execute(headers, values).body().accounts;
      return accounts.stream().map(account -> account.address).collect(Collectors.toList());
    } catch (Exception e) {
      throw new RuntimeException();
    }
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
          "An error occurs calling algorand blockchain. Response code {}. Message {}. AppId {}. Address {}",
          response.code(), response.message(), optinAppRequest.getAppId(),
          optinAppRequest.getAccount().getAddress().toString());
      throw new AlgorandInteractionError(response.code(), response.message());
    }
  }
}