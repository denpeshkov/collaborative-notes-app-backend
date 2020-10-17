package com.github.denpeshkov.authenticationservice.exception;

import com.github.denpeshkov.commons.exception.RestExceptionResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.nio.file.AccessDeniedException;
import java.util.HashMap;
import java.util.Map;

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
  public ResponseEntity<Object> handleAccessDeniedException(AccessDeniedException exception) {
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
  public ResponseEntity<Object> handleAuthenticationException(AuthenticationException exception) {
    RestExceptionResponse exceptionResponse =
        new RestExceptionResponse(
            HttpStatus.UNAUTHORIZED,
            "Token for an authentication request or for an authenticated principal is invalid",
            exception);

    return handleExceptionInternal(
        exception, exceptionResponse, null, exceptionResponse.getStatus(), null);
  }

  @Override
  protected ResponseEntity<Object> handleMethodArgumentNotValid(
      MethodArgumentNotValidException exception,
      HttpHeaders headers,
      HttpStatus status,
      WebRequest request) {
    Map<String, String> errors = new HashMap<>();
    exception
        .getBindingResult()
        .getAllErrors()
        .forEach(
            (error) -> {
              String fieldName = ((FieldError) error).getField();
              String errorMessage = error.getDefaultMessage();
              errors.put(fieldName, errorMessage);
            });

    RestExceptionResponse exceptionResponse =
        new RestExceptionResponse(status, errors.toString(), exception);

    return handleExceptionInternal(exception, exceptionResponse, headers, status, request);
  }

  @Override
  protected ResponseEntity<Object> handleHttpMessageNotReadable(
      HttpMessageNotReadableException exception,
      HttpHeaders headers,
      HttpStatus status,
      WebRequest request) {
    RestExceptionResponse exceptionResponse =
        new RestExceptionResponse(status, "Incorrect HTTP message", exception);

    return handleExceptionInternal(exception, exceptionResponse, headers, status, request);
  }
}
