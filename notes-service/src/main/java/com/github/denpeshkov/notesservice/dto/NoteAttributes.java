package com.github.denpeshkov.notesservice.dto;

import com.github.denpeshkov.notesservice.Note;

/** Note without data. Only name and other attributes useful for listing notes and preview */
public class NoteAttributes {
  private final String title;

  public NoteAttributes(String title) {
    this.title = title;
  }

  public String getTitle() {
    return title;
  }

  @Override
  public String toString() {
    return "Note{" + "title='" + title + '\'' + '}';
  }
}
