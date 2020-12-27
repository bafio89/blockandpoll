package com.pollalgorand.rest.adapter.service;


import com.algorand.algosdk.account.Account;
import java.security.GeneralSecurityException;

public class AccountCreatorService {

  public Account createAccountFrom(String mnemonicKey) throws GeneralSecurityException {
    return new Account(mnemonicKey);
  }
}
