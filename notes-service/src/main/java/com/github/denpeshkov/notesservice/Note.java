package com.github.denpeshkov.notesservice;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/** Note entity */
@Entity
public class Note {

  private String title;
  private String text;

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

  public Note() {}

  public Note(String title, String text) {
    this.title = title;
    this.text = text;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Long getId() {
    return id;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String name) {
    this.title = name;
  }

  public String getText() {
    return text;
  }

  public void setText(String text) {
    this.text = text;
  }
}
