package com.pollalgorand.rest.web.endpoint;

import static org.springframework.http.ResponseEntity.status;

import com.algorand.algosdk.transaction.Transaction;
import com.pollalgorand.rest.domain.exceptions.IllegalPollParameterException;
import com.pollalgorand.rest.domain.model.BlockchainPoll;
import com.pollalgorand.rest.domain.model.Poll;
import com.pollalgorand.rest.domain.usecase.CreatePollUseCase;
import com.pollalgorand.rest.web.adapter.PollRequestAdapter;
import com.pollalgorand.rest.web.request.PollRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(headers = "Accept=application/json")
public class CreatePollEndPoint {

  private CreatePollUseCase createPollUseCase;
  private PollRequestAdapter pollRequestAdapter;

  private Logger logger = LoggerFactory.getLogger(CreatePollEndPoint.class);

  public CreatePollEndPoint(CreatePollUseCase createPollUseCase,
      PollRequestAdapter pollRequestAdapter) {
    this.createPollUseCase = createPollUseCase;
    this.pollRequestAdapter = pollRequestAdapter;
  }

  @PostMapping("/createpoll/signedtx")
  public ResponseEntity<BlockchainPoll> createPollTransaction(@RequestBody PollRequest pollRequest) {

    logger.info("Arrived request");
    Poll poll = pollRequestAdapter.fromRequestToDomain(pollRequest);
    logger.info("Request adapted to poll");
    BlockchainPoll createdPoll = createPollUseCase.create(poll);
    return ResponseEntity.ok(createdPoll);
  }

  @PostMapping("/createpoll/unsignedtx")
  public ResponseEntity<byte[]> createPollUnsignedTransaction(@RequestBody PollRequest pollRequest) {

    Poll poll = pollRequestAdapter.fromRequestToDomain(pollRequest);
    Transaction unsignedTx = createPollUseCase.createUnsignedTx(poll);
    return ResponseEntity.ok(pollRequestAdapter.fromDomainToRequest(unsignedTx));
  }

  @ExceptionHandler(value = {IllegalPollParameterException.class})
  public ResponseEntity preconditionFailedExceptionHandler(RuntimeException e) {
    logger.error("An error occurred caused by wrong parameters. " , e);
    return status(HttpStatus.PRECONDITION_FAILED).body(e.getMessage());
  }

  @ExceptionHandler(value = {RuntimeException.class})
  public ResponseEntity genericErrorExceptionHandler(RuntimeException e){
    logger.error("An error occurred. " , e);
    return status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
  }
}
