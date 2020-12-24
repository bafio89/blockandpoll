package com.pollalgorand.rest.web.request;

import java.util.Objects;

public class OptinRequest {

  private String sender;
  private String mnemonicKey;

  public OptinRequest() {
  }

  public OptinRequest(String sender, String mnemonicKey) {

    this.sender = sender;
    this.mnemonicKey = mnemonicKey;
  }

  public String getSender() {
    return sender;
  }

  public String getMnemonicKey() {
    return mnemonicKey;
  }

  @Override
  public String toString() {
    return "OptinRequest{" +
        "sender='" + sender + '\'' +
        ", mnemonicKey='" + mnemonicKey + '\'' +
        '}';
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    OptinRequest that = (OptinRequest) o;
    return Objects.equals(sender, that.sender) &&
        Objects.equals(mnemonicKey, that.mnemonicKey);
  }

  @Override
  public int hashCode() {
    return Objects.hash(sender, mnemonicKey);
  }
}
