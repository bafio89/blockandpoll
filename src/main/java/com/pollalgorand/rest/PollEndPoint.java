package com.pollalgorand.rest;

import static org.springframework.http.ResponseEntity.status;

import com.algorand.algosdk.transaction.Transaction;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(headers = "Accept=application/json")
public class PollEndPoint {

  private CreatePollUseCase createPollUseCase;
  private PollRequestAdapter pollRequestAdapter;

  public PollEndPoint(CreatePollUseCase createPollUseCase,
      PollRequestAdapter pollRequestAdapter) {
    this.createPollUseCase = createPollUseCase;
    this.pollRequestAdapter = pollRequestAdapter;
  }

  @PostMapping("/createpoll/unsignedtx")
  public ResponseEntity<Transaction> createPollUnsignedTransaction(@RequestBody PollRequest poll){

    return ResponseEntity.ok(createPollUseCase
        .createUnsignedTx(pollRequestAdapter.fromRequestToDomain(poll)));
  }

  @ExceptionHandler(
      value = {
          IllegalPollParameterException.class
      })
  public ResponseEntity preconditionFailedExceptionHandler(RuntimeException e) {
    return status(HttpStatus.PRECONDITION_FAILED).body(e.getMessage());
  }
}
