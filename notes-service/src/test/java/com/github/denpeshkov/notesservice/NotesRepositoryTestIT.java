package com.github.denpeshkov.notesservice;

import com.github.denpeshkov.notesservice.dto.NoteAttributes;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
class NotesRepositoryTestIT {
  @Autowired private NotesRepository notesRepository;

  @BeforeEach
  void setUp() {
    notesRepository.save(new Note("title1", "text1"));
    notesRepository.save(new Note("title2", "text2"));
    notesRepository.save(new Note("title3", "text3"));
  }

  @Test
  void findByUsernameWhenNoUserExists() {
    List<NoteAttributes> notes = notesRepository.findAllBy();

    System.out.println(notes);
  }
}
