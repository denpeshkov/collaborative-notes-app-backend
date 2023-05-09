package com.github.denpeshkov.authenticationservice.exception;

import com.github.denpeshkov.commons.exception.RestExceptionResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.HashMap;
import java.util.Map;

/** Handles all REST exceptions */
// Extends from ResponseEntityExceptionHandler to inherit basic exceptions handlers, so we
// don't need to create them
@RestControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {
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

  /**
   * Customize the response for {@link UserAlreadyExistsException}.
   *
   * <p>This method delegates to {@link #handleExceptionInternal}.
   *
   * @param exception the exception
   * @return a {@link ResponseEntity} instance
   */
  @ExceptionHandler(UserAlreadyExistsException.class)
  public ResponseEntity<Object> handleUserAlreadyExistsException(
      UserAlreadyExistsException exception) {
    RestExceptionResponse exceptionResponse =
        new RestExceptionResponse(HttpStatus.UNAUTHORIZED, "User already exists!", exception);

    return handleExceptionInternal(
        exception, exceptionResponse, null, exceptionResponse.getStatus(), null);
  }

  /**
   * Customize the response for {@link UserNotFoundException}.
   *
   * <p>This method delegates to {@link #handleExceptionInternal}.
   *
   * @param exception the exception
   * @return a {@link ResponseEntity} instance
   */
  @ExceptionHandler(UserNotFoundException.class)
  public ResponseEntity<Object> handleUserNotFoundException(UserNotFoundException exception) {
    RestExceptionResponse exceptionResponse =
        new RestExceptionResponse(HttpStatus.UNAUTHORIZED, "User not found!", exception);

    return handleExceptionInternal(
        exception, exceptionResponse, null, exceptionResponse.getStatus(), null);
  }

  /**
   * Customize the response for {@link UserNotFoundException}.
   *
   * <p>This method delegates to {@link #handleExceptionInternal}.
   *
   * @param exception the exception
   * @return a {@link ResponseEntity} instance
   */
  @ExceptionHandler(IncorrectPasswordException.class)
  public ResponseEntity<Object> handleIncorrectPasswordException(
      IncorrectPasswordException exception) {
    RestExceptionResponse exceptionResponse =
        new RestExceptionResponse(HttpStatus.UNAUTHORIZED, "Incorrect password!", exception);

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
