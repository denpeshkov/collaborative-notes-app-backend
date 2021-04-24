package com.github.denpeshkov.notesservice;

import com.github.denpeshkov.notesservice.dto.DtoConverter;
import com.github.denpeshkov.notesservice.dto.NoteAttributes;
import com.github.denpeshkov.notesservice.exception.NoteNotExistsException;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

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

  @GetMapping("/{id}")
  @ResponseStatus(HttpStatus.OK)
  @ApiOperation("Вернуть записку по id")
  public Note getNote(@PathVariable @ApiParam("Id записки") Long id) throws NoteNotExistsException {
    return notesService.getNote(id);
  }

  @PostMapping
  @ApiOperation("Добавить записку")
  public long addNote(@RequestBody NoteAttributes noteDto) {
    Note note = DtoConverter.convert(noteDto);
    return notesService.saveNote(note);
  }

  @PutMapping("/{id}")
  public void editNote(@RequestBody Note note, @PathVariable @ApiParam("Id записки") Long id)
      throws NoteNotExistsException {
    Note existingNote = notesService.getNote(id);
    if (note.getTitle() != null) existingNote.setTitle(note.getTitle());
    if (note.getText() != null) existingNote.setText(note.getText());
    notesService.saveNote(existingNote);
  }

  @DeleteMapping("/{id}")
  @ApiOperation("Удалить записку")
  public void deleteNote(@PathVariable @ApiParam("Id записки для удаления") Long id)
      throws NoteNotExistsException {
    notesService.deleteNote(id);
  }
}
