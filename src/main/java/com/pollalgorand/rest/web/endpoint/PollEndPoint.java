package com.pollalgorand.rest.web.endpoint;

import static org.springframework.http.ResponseEntity.status;

import com.algorand.algosdk.transaction.Transaction;
import com.pollalgorand.rest.domain.exceptions.IllegalPollParameterException;
import com.pollalgorand.rest.domain.model.BlockchainPoll;
import com.pollalgorand.rest.domain.model.Poll;
import com.pollalgorand.rest.domain.usecase.CreatePollUseCase;
import com.pollalgorand.rest.web.adapter.PollRequestAdapter;
import com.pollalgorand.rest.web.request.PollRequest;
import java.util.Optional;
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
public class PollEndPoint {

  private CreatePollUseCase createPollUseCase;
  private PollRequestAdapter pollRequestAdapter;

  private Logger logger = LoggerFactory.getLogger(PollEndPoint.class);

  public PollEndPoint(CreatePollUseCase createPollUseCase,
      PollRequestAdapter pollRequestAdapter) {
    this.createPollUseCase = createPollUseCase;
    this.pollRequestAdapter = pollRequestAdapter;
  }

  @PostMapping("/createpoll/signedtx")
  public ResponseEntity<BlockchainPoll> createPollTransaction(@RequestBody PollRequest pollRequest) {

    logger.info("Arrived request");
    Poll poll = pollRequestAdapter.fromRequestToDomain(pollRequest);
    Optional<BlockchainPoll> createdPoll = createPollUseCase.create(poll);
    return ResponseEntity.ok(createdPoll.get());
  }

  @PostMapping("/createpoll/unsignedtx")
  public ResponseEntity<byte[]> createPollUnsignedTransaction(@RequestBody PollRequest pollRequest) {

    Poll poll = pollRequestAdapter.fromRequestToDomain(pollRequest);
    Transaction unsignedTx = createPollUseCase.createUnsignedTx(poll);
    return ResponseEntity.ok(pollRequestAdapter.fromDomainToRequest(unsignedTx));
  }

  @ExceptionHandler(value = {IllegalPollParameterException.class})
  public ResponseEntity preconditionFailedExceptionHandler(RuntimeException e) {
    return status(HttpStatus.PRECONDITION_FAILED).body(e.getMessage());
  }

  @ExceptionHandler(value = {RuntimeException.class})
  public ResponseEntity genericErrorExceptionHandler(RuntimeException e){
    return status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
  }
}
