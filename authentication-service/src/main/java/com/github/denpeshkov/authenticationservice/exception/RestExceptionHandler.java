package com.github.denpeshkov.authenticationservice.exception;

import com.github.denpeshkov.commons.exception.RestExceptionResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.nio.file.AccessDeniedException;

/** Handles all REST exceptions */
// Extends from ResponseEntityExceptionHandler to inherit basic exceptions handlers, so we
// don't need to create them
@RestControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

  @ExceptionHandler(AccessDeniedException.class)
  protected ResponseEntity<Object> handleAccessDeniedException(AccessDeniedException exception) {
    RestExceptionResponse exceptionResponse =
        new RestExceptionResponse(
            HttpStatus.FORBIDDEN, "User doesn't have required authorities", exception);

    return handleExceptionInternal(
        exception, exceptionResponse, null, exceptionResponse.getStatus(), null);
  }

  @ExceptionHandler(AuthenticationException.class)
  protected ResponseEntity<Object> handleAuthenticationException(
      AuthenticationException exception) {
    RestExceptionResponse exceptionResponse =
        new RestExceptionResponse(
            HttpStatus.UNAUTHORIZED,
            "Token for an authentication request or for an authenticated principal is invalid",
            exception);

    return handleExceptionInternal(
        exception, exceptionResponse, null, exceptionResponse.getStatus(), null);
  }
}
