package app.netbooks.backend.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import app.netbooks.backend.dtos.response.PublisherResponse;
import app.netbooks.backend.models.Publisher;
import app.netbooks.backend.services.PublisherService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/publishers")
public class PublisherController {
    @Autowired
    private PublisherService service;

    @GetMapping
    public ResponseEntity<List<PublisherResponse>> get() {
        List<Publisher> publishers = service.findAll();
        List<PublisherResponse> response = PublisherResponse.fromList(publishers);
        return ResponseEntity.ok().body(response);
    };

    @PostMapping
    public ResponseEntity<Void> create(@RequestBody Publisher publisher) {
        service.create(publisher);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    };

    @GetMapping("/search")
    public ResponseEntity<List<PublisherResponse>> searchByName(
        @RequestParam(required = false, defaultValue = "") String name
    ) {
        List<Publisher> publishers = service.searchByName(name);
        List<PublisherResponse> response = PublisherResponse.fromList(publishers);
        
        return ResponseEntity.ok().body(response);
    };
};
