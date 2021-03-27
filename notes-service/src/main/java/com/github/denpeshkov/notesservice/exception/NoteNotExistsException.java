package com.github.denpeshkov.notesservice.exception;

public class NoteNotExistsException extends Exception {

  public NoteNotExistsException() {
    super();
  }

  public NoteNotExistsException(String message) {
    super(message);
  }

  public NoteNotExistsException(String message, Throwable cause) {
    super(message, cause);
  }

  public NoteNotExistsException(Throwable cause) {
    super(cause);
  }
}
