package com.pollalgorand.rest.web.endpoint;

import static org.springframework.http.ResponseEntity.status;

import com.pollalgorand.rest.domain.exceptions.OptinAlreadDoneException;
import com.pollalgorand.rest.domain.usecase.OptinUseCase;
import com.pollalgorand.rest.web.request.OptinRequest;
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
public class OptinPollEndPoint {

  private Logger logger = LoggerFactory.getLogger(OptinPollEndPoint.class);
  private OptinUseCase optinUseCase;
  private OptinRequestConverter optinRequestConverter;


  public OptinPollEndPoint(OptinUseCase optinUseCase,
      OptinRequestConverter optinRequestConverter) {

    this.optinUseCase = optinUseCase;
    this.optinRequestConverter = optinRequestConverter;
  }

  @PostMapping("/optin/poll/{appId}")
  public ResponseEntity<Void> optin(@PathVariable long appId, @RequestBody OptinRequest request){

    optinUseCase.optin(optinRequestConverter.fromRequestToDomain(appId, request));
    return ResponseEntity.ok().build();
  }

  @ExceptionHandler(OptinAlreadDoneException.class)
  public ResponseEntity preconditionFailedExceptionHandler(RuntimeException e) {
    return status(HttpStatus.PRECONDITION_FAILED).body(e.getMessage());
  }

  @ExceptionHandler(RuntimeException.class)
  public ResponseEntity serverError(RuntimeException e){
    return status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
  }
}
