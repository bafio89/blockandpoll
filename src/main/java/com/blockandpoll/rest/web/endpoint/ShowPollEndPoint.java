package com.blockandpoll.rest.web.endpoint;

import static org.springframework.http.ResponseEntity.status;

import com.blockandpoll.rest.adapter.exceptions.AlgorandInteractionError;
import com.blockandpoll.rest.adapter.exceptions.ApplicationNotFoundException;
import com.blockandpoll.rest.domain.model.BlockchainPoll;
import com.blockandpoll.rest.domain.model.EnrichedBlockchainPoll;
import com.blockandpoll.rest.domain.usecase.RetrievePollUseCase;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(headers = "Accept=application/json")
public class ShowPollEndPoint {

  private Logger logger = LoggerFactory.getLogger(ShowPollEndPoint.class);

  private RetrievePollUseCase retrievePollUseCase;

  public ShowPollEndPoint(
      RetrievePollUseCase retrievePollUseCase) {
    this.retrievePollUseCase = retrievePollUseCase;
  }

  @GetMapping("/polls")
  public ResponseEntity<List<BlockchainPoll>> retrievePolls() {

    logger.info("Arrived request for poll retrieving");
    return ResponseEntity.ok(retrievePollUseCase.retrievePolls());
  }

  @GetMapping("/poll/{appId}")
  public ResponseEntity<EnrichedBlockchainPoll> retrievePollBy(@PathVariable long appId) {

    logger.info("Arrived request for poll retrieving");
    return ResponseEntity.ok(retrievePollUseCase.findPollByAppId(appId));
  }

  @ExceptionHandler(AlgorandInteractionError.class)
  public ResponseEntity serverError(RuntimeException e) {
    logger.error("An error occurred.", e);
    return status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
  }

  @ExceptionHandler(ApplicationNotFoundException.class)
  public ResponseEntity resourceNotFound(RuntimeException e) {
    return status(HttpStatus.NOT_FOUND).body(e.getMessage());
  }

}
