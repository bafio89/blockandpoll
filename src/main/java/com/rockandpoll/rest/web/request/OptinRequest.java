package com.rockandpoll.rest.web.request;

import java.util.Objects;

public class OptinRequest {

  private String mnemonicKey;

  public OptinRequest() {
  }

  public OptinRequest(String mnemonicKey) {

    this.mnemonicKey = mnemonicKey;
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
    OptinRequest that = (OptinRequest) o;
    return Objects.equals(mnemonicKey, that.mnemonicKey);
  }

  @Override
  public int hashCode() {
    return Objects.hash(mnemonicKey);
  }

  @Override
  public String toString() {
    return "OptinRequest{" +
        "mnemonicKey='" + mnemonicKey + '\'' +
        '}';
  }
}
