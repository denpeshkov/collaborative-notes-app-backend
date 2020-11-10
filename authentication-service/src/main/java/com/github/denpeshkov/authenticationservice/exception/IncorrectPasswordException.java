package com.github.denpeshkov.authenticationservice.exception;

public class IncorrectPasswordException extends AuthenticationException {
  public IncorrectPasswordException() {}

  public IncorrectPasswordException(String message) {
    super(message);
  }

  public IncorrectPasswordException(String message, Throwable cause) {
    super(message, cause);
  }

  public IncorrectPasswordException(Throwable cause) {
    super(cause);
  }
}
