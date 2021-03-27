package com.github.denpeshkov.notesservice.exception;

import com.github.denpeshkov.commons.exception.RestExceptionResponse;
import java.util.HashMap;
import java.util.Map;
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

/** Handles all REST exceptions */
// Extends from ResponseEntityExceptionHandler to inherit basic exceptions handlers, so we
// don't need to create them
@RestControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

  @ExceptionHandler(NoteAlreadyExistsException.class)
  public ResponseEntity<Object> handleAuthenticationException(
      NoteAlreadyExistsException exception) {
    RestExceptionResponse exceptionResponse =
        new RestExceptionResponse(HttpStatus.CONFLICT, "Note already exists!", exception);

    return handleExceptionInternal(
        exception, exceptionResponse, null, exceptionResponse.getStatus(), null);
  }

  @ExceptionHandler(NoteNotExistsException.class)
  public ResponseEntity<Object> handleAuthenticationException(NoteNotExistsException exception) {
    RestExceptionResponse exceptionResponse =
        new RestExceptionResponse(HttpStatus.CONFLICT, "Note not exists!", exception);

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
