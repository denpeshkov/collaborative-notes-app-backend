package com.github.denpeshkov.notesservice.exception;

/** Exception that can be thrown when note already exists */
public class NoteAlreadyExistsException extends Exception {
  public NoteAlreadyExistsException() {
    super();
  }

  public NoteAlreadyExistsException(String message) {
    super(message);
  }

  public NoteAlreadyExistsException(String message, Throwable cause) {
    super(message, cause);
  }

  public NoteAlreadyExistsException(Throwable cause) {
    super(cause);
  }
}
