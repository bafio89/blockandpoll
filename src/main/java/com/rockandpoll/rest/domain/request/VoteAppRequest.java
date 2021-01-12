package com.rockandpoll.rest.domain.request;

import com.algorand.algosdk.account.Account;
import java.util.Objects;

public class VoteAppRequest {

  private final long appId;
  private final Account account;
  private final String selectedOption;

  public VoteAppRequest(long appId, Account account, String selectedOption) {
    this.appId = appId;
    this.account = account;
    this.selectedOption = selectedOption;
  }

  public long getAppId() {
    return appId;
  }

  public Account getAccount() {
    return account;
  }

  public String getSelectedOption() {
    return selectedOption;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    VoteAppRequest that = (VoteAppRequest) o;
    return appId == that.appId &&
        Objects.equals(account, that.account) &&
        Objects.equals(selectedOption, that.selectedOption);
  }

  @Override
  public int hashCode() {
    return Objects.hash(appId, account, selectedOption);
  }

  @Override
  public String toString() {
    return "VoteAppRequest{" +
        "appId=" + appId +
        ", account=" + account +
        ", anOption='" + selectedOption + '\'' +
        '}';
  }
}
