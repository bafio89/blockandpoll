package com.pollalgorand.rest.domain.request;

import com.algorand.algosdk.account.Account;
import java.util.Objects;

public class OptinAppRequest {

  private final long appId;
  private final Account account;

  public OptinAppRequest(long appId, Account account) {

    this.appId = appId;
    this.account = account;
  }

  public long getAppId() {
    return appId;
  }

  public Account getAccount() {
    return account;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    OptinAppRequest that = (OptinAppRequest) o;
    return appId == that.appId &&
        Objects.equals(account, that.account);
  }

  @Override
  public int hashCode() {
    return Objects.hash(appId, account);
  }
}
