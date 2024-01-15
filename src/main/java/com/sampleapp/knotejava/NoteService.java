package com.sampleapp.knotejava;

import org.commonmark.node.Node;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.HtmlRenderer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.multipart.MultipartFile;
import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.nio.file.Files;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Service
public class NoteService {

    @Autowired
    private NotesRepository notesRepository;

    @Autowired
    private NoteProperties noteProperties;

    private Parser parser = Parser.builder().build();
    private HtmlRenderer renderer = HtmlRenderer.builder().build();

    public void getAllNotes(Model model) {
        List<Note> notes = notesRepository.findAll();
        Collections.reverse(notes);
        model.addAttribute("notes", notes);
    }

    public void saveNote(String description, Model model) {
        if (description != null && !description.trim().isEmpty()) {
            // translate markup to html
            Node document = parser.parse(description.trim());
            String html = renderer.render(document);
            notesRepository.save(new Note(null, html));

            // clean up textarea
            model.addAttribute("description", "");
        }
    }

    public void uploadImage(MultipartFile file, String description, Model model) throws IOException {
        File uploadsDir = new File(noteProperties.getUploadDir());
        if (!uploadsDir.exists()) {
            boolean result = uploadsDir.mkdirs();
            System.out.println(result);
        }
        String fileId = UUID.randomUUID().toString() + "." +
                file.getOriginalFilename().split("\\.")[1];
        String destinationFile = noteProperties.getUploadDir() + fileId;
        try (InputStream inputStream = file.getInputStream()) {
            Files.copy(inputStream, Paths.get(destinationFile),
                    StandardCopyOption.REPLACE_EXISTING);
        }
        model.addAttribute("description",
                description + " ![](/uploads/" + fileId + ")");
    }
}
