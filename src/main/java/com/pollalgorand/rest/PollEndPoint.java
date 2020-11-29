package com.pollalgorand.rest;

import com.algorand.algosdk.v2.client.model.Transaction;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PollEndPoint {

  private CreatePollUseCase createPollUseCase;
  private PollRequestConverter pollRequestConverter;

  public PollEndPoint(CreatePollUseCase createPollUseCase) {
    this.createPollUseCase = createPollUseCase;
  }

  @PostMapping("/createpoll/unsignedtx")
  public ResponseEntity<Transaction> createPollUnsignedTransaction(@RequestBody PollRequest poll){

    com.algorand.algosdk.transaction.Transaction unsignedTx = createPollUseCase
        .createUnsignedTx(pollRequestConverter.fromRequestToDomain(poll));
    return ResponseEntity.of(null);
  }


}
