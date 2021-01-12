package com.blockandpoll.rest.web.request;

import java.util.Objects;

public class VoteRequest {

  private String mnemonicKey;
  private String selectedOption;

  public VoteRequest() {
  }

  public VoteRequest(String mnemonicKey, String selectedOption) {

    this.mnemonicKey = mnemonicKey;
    this.selectedOption = selectedOption;
  }

  public String getMnemonicKey() {
    return mnemonicKey;
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
    VoteRequest that = (VoteRequest) o;
    return Objects.equals(mnemonicKey, that.mnemonicKey) &&
        Objects.equals(selectedOption, that.selectedOption);
  }

  @Override
  public int hashCode() {
    return Objects.hash(mnemonicKey, selectedOption);
  }

  @Override
  public String toString() {
    return "VoteRequest{" +
        "mnemonicKey='" + mnemonicKey + '\'' +
        ", selectedOption='" + selectedOption + '\'' +
        '}';
  }
}
