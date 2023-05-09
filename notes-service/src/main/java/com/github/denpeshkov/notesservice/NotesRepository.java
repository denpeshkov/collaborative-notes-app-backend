package com.github.denpeshkov.notesservice;

import com.github.denpeshkov.notesservice.dto.NoteAttributes;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotesRepository extends JpaRepository<Note, Long> {
  List<NoteAttributes> findAllBy();
}
