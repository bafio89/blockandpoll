package com.pollalgorand.rest.web.adapter;

import com.algorand.algosdk.transaction.Transaction;
import com.pollalgorand.rest.domain.model.Poll;
import com.pollalgorand.rest.web.request.PollRequest;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

public class PollRequestAdapter {

  public Poll fromRequestToDomain(PollRequest poll) {
    return new Poll(poll.getName(),
        poll.getStartSubscriptionTime(),
        poll.getEndSubscriptionTime(),
        poll.getStartVotingTime(),
        poll.getEndVotingTime(),
        poll.getOptions(),
        poll.getSender(),
        poll.getMnemonicKey(), poll.getDescription());
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
