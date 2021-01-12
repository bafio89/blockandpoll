package com.rockandpoll.rest.web.endpoint;

import static org.springframework.http.ResponseEntity.status;

import com.rockandpoll.rest.adapter.exceptions.InvalidMnemonicKeyException;
import com.rockandpoll.rest.adapter.exceptions.OptinException;
import com.rockandpoll.rest.domain.exceptions.OptinAlreadyDoneException;
import com.rockandpoll.rest.domain.exceptions.PollNotFoundException;
import com.rockandpoll.rest.domain.usecase.OptinUseCase;
import com.rockandpoll.rest.web.adapter.OptinRequestConverter;
import com.rockandpoll.rest.web.request.OptinRequest;
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
  public ResponseEntity<Void> optin(@PathVariable long appId, @RequestBody OptinRequest optinRequest) {
    logger.info("Arrived a request");
    optinUseCase.optin(optinRequestConverter.fromRequestToDomain(appId, optinRequest));
    return ResponseEntity.ok().build();
  }

  @ExceptionHandler({OptinAlreadyDoneException.class, PollNotFoundException.class})
  public ResponseEntity preconditionFailedExceptionHandler(RuntimeException e) {
    return status(HttpStatus.PRECONDITION_FAILED).body(e.getMessage());
  }

  @ExceptionHandler({RuntimeException.class, InvalidMnemonicKeyException.class, OptinException.class})
  public ResponseEntity serverError(RuntimeException e) {
    return status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
  }
}
