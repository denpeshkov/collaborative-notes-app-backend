package com.github.denpeshkov.notesservice.dto;

import com.github.denpeshkov.notesservice.Note;

public class DtoConverter {
  private DtoConverter() {}

  public static Note convert(NoteAttributes noteAttributes) {
    return new Note(noteAttributes.getTitle(), "");
  }

  public static NoteAttributes convert(Note note) {
    return new NoteAttributes(note.getTitle(), note.getId());
  }
}
