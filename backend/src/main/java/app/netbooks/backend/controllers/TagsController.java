package app.netbooks.backend.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import app.netbooks.backend.models.Tag;
import app.netbooks.backend.services.TagsService;

@RestController
@RequestMapping("/api/tags")
public class TagsController {
    @Autowired
    private TagsService service;

    @GetMapping
    public ResponseEntity<List<Tag>> get() {
        List<Tag> tags = service.findAll();
        return ResponseEntity.ok().body(tags);
    };
};
