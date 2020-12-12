package com.pollalgorand.rest.web.endpoint;

import com.pollalgorand.rest.domain.model.BlockchainPoll;
import com.pollalgorand.rest.domain.usecase.RetrievePollUseCase;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
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
  public ResponseEntity<List<BlockchainPoll>> retrievePolls(){

    logger.info("Arrived request for poll retrieving");
    return ResponseEntity.ok(retrievePollUseCase.retrievePolls());
  }


}
