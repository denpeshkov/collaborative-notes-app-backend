package com.github.denpeshkov.apigateway.security.exception;

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

  /**
   * Customize the response for {@link AccessDeniedException}.
   *
   * <p>This method delegates to {@link #handleExceptionInternal}.
   *
   * @param exception the exception
   * @return a {@link ResponseEntity} instance
   */
  @ExceptionHandler(AccessDeniedException.class)
  protected ResponseEntity<Object> handleAccessDeniedException(AccessDeniedException exception) {
    RestExceptionResponse exceptionResponse =
        new RestExceptionResponse(
            HttpStatus.FORBIDDEN, "User doesn't have required authorities", exception);

    return handleExceptionInternal(
        exception, exceptionResponse, null, exceptionResponse.getStatus(), null);
  }

  /**
   * Customize the response for {@link AuthenticationException}.
   *
   * <p>This method delegates to {@link #handleExceptionInternal}.
   *
   * @param exception the exception
   * @return a {@link ResponseEntity} instance
   */
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
