package com.pollalgorand.rest.web.endpoint;

import com.pollalgorand.rest.domain.usecase.VoteUseCase;
import com.pollalgorand.rest.web.adapter.VoteRequestConverter;
import com.pollalgorand.rest.web.request.VoteRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
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
    logger.info("Arrived a request");
    voteUseCase.vote(voteRequestConverter.fromRequestToDomain(appId, voteRequest));
    return ResponseEntity.ok().build();
  }

}
