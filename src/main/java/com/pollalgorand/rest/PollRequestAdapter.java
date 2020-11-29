package com.pollalgorand.rest;

import com.algorand.algosdk.transaction.Transaction;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

public class PollRequestAdapter {

  public Poll fromRequestToDomain(PollRequest poll) {
    return new Poll(poll.getName(),
        poll.getStartSubscriptionTime(),
        poll.getStartSubscriptionTime(),
        poll.getStartVotingTime(),
        poll.getEndVotingTime(),
        poll.getOptions(),
        poll.getSender());
  }

  public byte [] fromDomainToRequest(Transaction transaction){
    ByteArrayOutputStream bos = new ByteArrayOutputStream();
    ObjectOutputStream oos;
    try {
      oos = new ObjectOutputStream(bos);
    oos.writeObject(transaction);
    oos.flush();
    } catch (IOException e) {
      e.printStackTrace();
    }
    return bos.toByteArray();
  }
}
