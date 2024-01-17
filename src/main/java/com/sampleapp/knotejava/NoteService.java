package com.sampleapp.knotejava;

import org.commonmark.node.Node;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.HtmlRenderer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Service
public class NoteService {

    @Autowired
    NoteConfig noteConfig;

    @Autowired
    private NotesRepository notesRepository;

    @Autowired
    private NoteProperties properties;

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

    public void uploadImage(MultipartFile file, String description, Model model) throws Exception {
        String fileId = UUID.randomUUID().toString() + "." + file.getOriginalFilename().split("\\.")[1];
        noteConfig.getMinioClient().putObject(properties.getMinioBucket(), fileId, file.getInputStream(),
                file.getSize(), null, null, file.getContentType());
        model.addAttribute("description",
                description + " ![](/img/" + fileId + ")");
    }

    public InputStream getImageByName(String name) throws Exception {
        return noteConfig.getMinioClient().getObject(properties.getMinioBucket(), name);
    }
}
