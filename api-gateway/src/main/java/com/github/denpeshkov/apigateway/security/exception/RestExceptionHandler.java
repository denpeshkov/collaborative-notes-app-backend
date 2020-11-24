package com.github.denpeshkov.apigateway.security.exception;

import com.github.denpeshkov.commons.exception.RestExceptionResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

/** Handles all REST exceptions */
// Extends from ResponseEntityExceptionHandler to inherit basic exceptions handlers, so we
// don't need to create them
@RestControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

  /**
   * Customize the response for {@link org.springframework.security.access.AccessDeniedException
   * AccessDeniedException}.
   *
   * <p>This method delegates to {@link #handleExceptionInternal}.
   *
   * @param exception the exception
   * @return a {@link ResponseEntity} instance
   */
  @ExceptionHandler(org.springframework.security.access.AccessDeniedException.class)
  protected ResponseEntity<Object> handleAccessDeniedException(
      org.springframework.security.access.AccessDeniedException exception) {
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
            "There was an error during authentication process!",
            exception);

    return handleExceptionInternal(
        exception, exceptionResponse, null, exceptionResponse.getStatus(), null);
  }

  @Override
  protected ResponseEntity<Object> handleNoHandlerFoundException(
      NoHandlerFoundException exception,
      HttpHeaders headers,
      HttpStatus status,
      WebRequest request) {
    RestExceptionResponse exceptionResponse =
        new RestExceptionResponse(
            HttpStatus.NOT_FOUND,
            "No handler found! Check URL and headers of the request!",
            exception);

    return handleExceptionInternal(
        exception, exceptionResponse, null, exceptionResponse.getStatus(), null);
  }
}
