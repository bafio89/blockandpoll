package com.pollalgorand.rest.domain;

import java.util.Objects;

public class OptinAppRequest {

  private final long appId;
  private final String mnemonicKey;

  public OptinAppRequest(long appId, String mnemonicKey) {

    this.appId = appId;
    this.mnemonicKey = mnemonicKey;
  }

  public long getAppId() {
    return appId;
  }

  public String getMnemonicKey() {
    return mnemonicKey;
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
        Objects.equals(mnemonicKey, that.mnemonicKey);
  }

  @Override
  public int hashCode() {
    return Objects.hash(appId, mnemonicKey);
  }
}
