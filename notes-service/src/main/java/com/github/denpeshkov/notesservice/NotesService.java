package com.github.denpeshkov.notesservice;

import com.github.denpeshkov.notesservice.dto.NoteAttributes;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class NotesService {
  final NotesRepository notesRepository;

  public NotesService(NotesRepository notesRepository) {
    this.notesRepository = notesRepository;
  }

  /**
   * Gets list of all notes (preview)
   *
   * @return list of notes
   */
  public List<NoteAttributes> getAllNotes() {
    return notesRepository.findAllBy();
  }

  /**
   * Adds note
   *
   * @param note note
   * @return {@code true} if saved (new node added) {@code false} if not saved (duplicate node)
   */
  public boolean addNote(Note note) {
    if (notesRepository.existsById(note.getId())) return false;

    notesRepository.save(note);
    return true;
  }

  public void deleteNote(long id) {
    notesRepository.deleteById(id);
  }
}
