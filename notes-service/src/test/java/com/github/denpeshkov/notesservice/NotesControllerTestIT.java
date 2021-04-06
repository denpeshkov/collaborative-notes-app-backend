package com.github.denpeshkov.notesservice;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.github.denpeshkov.notesservice.dto.NoteAttributes;
import com.github.denpeshkov.notesservice.exception.NoteNotExistsException;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(NotesController.class)
@ExtendWith(MockitoExtension.class)
public class NotesControllerTestIT {
  @MockBean NotesService notesService;

  @Autowired private MockMvc mockMvc;

  @Captor ArgumentCaptor<Note> noteCaptor;

  @Test
  void getNotes() throws Exception {
    when(notesService.getAllNotes())
        .thenReturn(
            List.of(
                new NoteAttributes("title1", 1L),
                new NoteAttributes("title2", 2L),
                new NoteAttributes("title3", 3L)));

    mockMvc
        .perform(get("/api/notes"))
        .andExpect(status().isOk())
        .andExpect(
            content()
                .json(
                    "[{\"title\":\"title1\",\"id\":1},{\"title\":\"title2\",\"id\":2},{\"title\":\"title3\",\"id\":3}]",
                    true))
        .andDo(print());
  }

  @Test
  void getNote() throws Exception {
    Note note = new Note("title1", "text1");
    note.setId(1L);
    when(notesService.getNote(1L)).thenReturn(note);

    mockMvc
        .perform(get("/api/notes/1"))
        .andExpect(status().isOk())
        .andExpect(content().json("{\"title\":\"title1\",\"text\":\"text1\"}", true))
        .andDo(print());
  }

  @Test
  void addNote() throws Exception {
    mockMvc
        .perform(
            post("/api/notes")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"title\":\"title1\"}")
                .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andDo(print());

    verify(notesService).addNote(noteCaptor.capture());

    Note note = noteCaptor.getValue();

    Assertions.assertNull(note.getId());
    Assertions.assertEquals(note.getTitle(), "title1");

    System.out.println(note);
  }
}
