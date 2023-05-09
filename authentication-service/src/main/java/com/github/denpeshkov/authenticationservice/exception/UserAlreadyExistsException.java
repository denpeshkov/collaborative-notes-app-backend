package com.github.denpeshkov.authenticationservice.exception;

/** Exception that can be thrown when user already exists */
public class UserAlreadyExistsException extends AuthenticationException {
  public UserAlreadyExistsException() {}

  public UserAlreadyExistsException(String message) {
    super(message);
  }

  public UserAlreadyExistsException(String message, Throwable cause) {
    super(message, cause);
  }

  public UserAlreadyExistsException(Throwable cause) {
    super(cause);
  }
}
