package com.blockandpoll.rest.web.endpoint;

import static org.springframework.http.ResponseEntity.status;

import com.blockandpoll.rest.adapter.exceptions.VoteException;
import com.blockandpoll.rest.domain.exceptions.AlreadyVotedException;
import com.blockandpoll.rest.domain.exceptions.OptinIntervalTimeException;
import com.blockandpoll.rest.domain.exceptions.PollNotFoundException;
import com.blockandpoll.rest.domain.exceptions.VoteIntervalTimeException;
import com.blockandpoll.rest.domain.usecase.VoteUseCase;
import com.blockandpoll.rest.web.adapter.VoteRequestConverter;
import com.blockandpoll.rest.web.request.VoteRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(headers = "Accept=application/json")
public class VotePollEndPoint {

  private Logger logger = LoggerFactory.getLogger(VotePollEndPoint.class);

  private final VoteUseCase voteUseCase;
  private final VoteRequestConverter voteRequestConverter;


  public VotePollEndPoint(VoteUseCase voteUseCase, VoteRequestConverter voteRequestConverter) {

    this.voteUseCase = voteUseCase;
    this.voteRequestConverter = voteRequestConverter;
  }

  @PostMapping("/vote/poll/{appId}")
  public ResponseEntity<Void> vote(@PathVariable long appId, @RequestBody VoteRequest voteRequest){
    logger.info("Arrived a request for appId {}", appId);
    voteUseCase.vote(voteRequestConverter.fromRequestToDomain(appId, voteRequest));
    return ResponseEntity.ok().build();
  }

  @ExceptionHandler(value = {AlreadyVotedException.class, VoteIntervalTimeException.class,
      OptinIntervalTimeException.class})
  public ResponseEntity preconditionFailedExceptionHandler(RuntimeException e) {
    logger.error("An error occurred.", e);
    return status(HttpStatus.PRECONDITION_FAILED).body(e.getMessage());
  }

  @ExceptionHandler(value = {PollNotFoundException.class})
  public ResponseEntity pollNotFoundExceptionHandler(RuntimeException e) {
    logger.error("An error occurred.", e);
    return status(HttpStatus.NOT_FOUND).body(e.getMessage());
  }

  @ExceptionHandler(value = {RuntimeException.class, VoteException.class})
  public ResponseEntity genericErrorHandler(RuntimeException e) {
    logger.error("Something went wrong voting the app. " , e);
    return status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
  }
}
