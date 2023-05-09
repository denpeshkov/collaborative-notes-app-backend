package com.github.denpeshkov.notesservice.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

/** Note without data. Only name and other attributes useful for listing notes and preview */
public class NoteAttributes {
  private final String title;

  @JsonProperty(access = JsonProperty.Access.READ_ONLY)
  private final Long id;

  public NoteAttributes(String title, Long id) {
    this.title = title;
    this.id = id;
  }

  public String getTitle() {
    return title;
  }

  public Long getId() {
    return id;
  }

  @Override
  public String toString() {
    return "Note{" + "title='" + title + '\'' + ", id=" + id + '}';
  }
}
