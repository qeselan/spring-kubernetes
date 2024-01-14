package com.sampleapp.knotejava;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.util.Collections;
import java.util.List;

@Service
public class NoteService {

    @Autowired
    private NotesRepository notesRepository;

    public void getAllNotes(Model model) {
        List<Note> notes = notesRepository.findAll();
        Collections.reverse(notes);
        model.addAttribute("notes", notes);
    }

    public void saveNote(String description, Model model) {
        if (description != null && !description.trim().isEmpty()) {
            notesRepository.save(new Note(null, description.trim()));
            model.addAttribute("description", "");
        }
    }
}
