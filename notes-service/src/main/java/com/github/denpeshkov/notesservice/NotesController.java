package com.github.denpeshkov.notesservice;

import com.github.denpeshkov.notesservice.dto.DtoConverter;
import com.github.denpeshkov.notesservice.dto.NoteAttributes;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin
@RestController
@RequestMapping("/api/notes")
public class NotesController {
  final NotesService notesService;

  public NotesController(NotesService notesService) {
    this.notesService = notesService;
  }

  @GetMapping
  @ResponseStatus(HttpStatus.OK)
  @ApiOperation("Вернуть список записок без тела")
  public List<NoteAttributes> getNotes() {
    return notesService.getAllNotes();
  }

  @PostMapping
  @ApiOperation("Добавить записку")
  public void addNote(NoteAttributes noteDto) {
    Note note = DtoConverter.convert(noteDto);
    notesService.addNote(note);
  }

  @DeleteMapping("/{id}")
  @ApiOperation("Удалить записку")
  public void deleteNote(@PathVariable @ApiParam("Id записки для удления") Long id) {
    notesService.deleteNote(id);
  }
}
