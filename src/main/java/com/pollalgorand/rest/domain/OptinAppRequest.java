package com.pollalgorand.rest.domain;

import java.util.Objects;

public class OptinAppRequest {

  private final long appId;
  private final String sender;
  private final String mnemonicKey;

  public OptinAppRequest(long appId, String sender, String mnemonicKey) {

    this.appId = appId;
    this.sender = sender;
    this.mnemonicKey = mnemonicKey;
  }

  public long getAppId() {
    return appId;
  }

  public String getSender() {
    return sender;
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
        Objects.equals(sender, that.sender) &&
        Objects.equals(mnemonicKey, that.mnemonicKey);
  }

  @Override
  public int hashCode() {
    return Objects.hash(appId, sender, mnemonicKey);
  }

  @Override
  public String toString() {
    return "OptinAppRequest{" +
        "appId=" + appId +
        ", sender='" + sender + '\'' +
        ", mnemonicKey='" + mnemonicKey + '\'' +
        '}';
  }
}
