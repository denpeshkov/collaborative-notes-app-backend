package com.github.denpeshkov.notesservice;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.github.denpeshkov.notesservice.dto.NoteAttributes;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(NotesController.class)
@ExtendWith(MockitoExtension.class)
public class NotesControllerTestIT {
  @MockBean NotesService notesService;

  @Autowired private MockMvc mockMvc;

  @Test
  void getNotes() throws Exception {
    when(notesService.getAllNotes())
        .thenReturn(
            List.of(
                new NoteAttributes("title1"),
                new NoteAttributes("title2"),
                new NoteAttributes("title3")));

    mockMvc
        .perform(get("/api/notes"))
        .andExpect(status().isOk())
        .andExpect(
            content()
                .json("[{\"title\":\"title1\"},{\"title\":\"title2\"},{\"title\":\"title3\"}]"))
        .andDo(print());
  }
}
