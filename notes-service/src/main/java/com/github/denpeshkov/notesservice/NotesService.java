package com.github.denpeshkov.notesservice;

import com.github.denpeshkov.notesservice.dto.NoteAttributes;
import com.github.denpeshkov.notesservice.exception.NoteAlreadyExistsException;
import com.github.denpeshkov.notesservice.exception.NoteNotExistsException;
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

  public Note getNote(Long id) throws NoteNotExistsException {
    if (!notesRepository.existsById(id))
      throw new NoteNotExistsException(String.format("Note with id=%d not exists", id));
    return notesRepository.findById(id).get();
  }

  /**
   * Adds note
   *
   * @param note note
   */
  public void addNote(Note note) throws NoteAlreadyExistsException {
    if (notesRepository.existsById(note.getId()))
      throw new NoteAlreadyExistsException(
          String.format("Note with id=%d already exits", note.getId()));

    notesRepository.save(note);
  }

  public void deleteNote(long id) throws NoteNotExistsException {
    if (!notesRepository.existsById(id))
      throw new NoteNotExistsException(String.format("Note with id=%d not exists", id));
    notesRepository.deleteById(id);
  }
}
