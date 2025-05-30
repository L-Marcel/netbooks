package app.netbooks.backend.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import app.netbooks.backend.models.Publisher;
import app.netbooks.backend.services.PublisherService;


@RestController
@RequestMapping("/publishers")
public class PublisherController {
    @Autowired
    private PublisherService service;

    @GetMapping
    public ResponseEntity<List<Publisher>> get() {
        List<Publisher> publishers = service.findAll();
        return ResponseEntity.ok().body(publishers);
    }
}
