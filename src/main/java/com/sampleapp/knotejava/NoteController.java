package com.sampleapp.knotejava;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import java.io.InputStream;

@Controller
@Slf4j
public class NoteController {

    @Autowired
    NoteService noteService;

    @Autowired
    NoteConfig noteConfig;

    @PostConstruct
    private void postConstruct() {
        noteConfig.initMinio();
    }

    @GetMapping("/")
    public String index(Model model) {
        noteService.getAllNotes(model);
        return "index";
    }

    @PostMapping("/note")
    public String saveNotes(@RequestParam("image") MultipartFile file,
                            @RequestParam String description,
                            @RequestParam(required = false) String publish,
                            @RequestParam(required = false) String upload,
                            Model model) throws Exception {

        if (publish != null && publish.equals("Publish")) {
            noteService.saveNote(description, model);
            noteService.getAllNotes(model);
            return "redirect:/";
        }

        if (upload != null && upload.equals("Upload")) {
            if (file != null && file.getOriginalFilename() != null && !file.getOriginalFilename().isEmpty()) {
                noteService.uploadImage(file, description, model);
            }
            noteService.getAllNotes(model);
            return "index";
        }
        // After save fetch all notes again
        return "index";
    }

    @GetMapping(value = "/img/{name}", produces = MediaType.IMAGE_PNG_VALUE)
    public @ResponseBody byte[] getImageByName(@PathVariable String name) throws Exception {
        InputStream imageStream = noteService.getImageByName(name);
        return IOUtils.toByteArray(imageStream);
    }
}
