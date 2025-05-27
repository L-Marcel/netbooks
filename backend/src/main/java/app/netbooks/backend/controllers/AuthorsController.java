package app.netbooks.backend.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import app.netbooks.backend.dtos.AuthorResponse;
import app.netbooks.backend.models.Author;
import app.netbooks.backend.services.AuthorsService;


@RestController
@RequestMapping("/authors")
public class AuthorsController {
    @Autowired
    private AuthorsService service;

    @GetMapping("/{name}")
    public ResponseEntity<List<AuthorResponse>> getAuthorsByName(
        @PathVariable String name
    ) {
        List<Author> authors = service.findAuthorsByName(name);
        List<AuthorResponse> response = AuthorResponse.fromList(authors);
        
        return ResponseEntity.ok().body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AuthorResponse> getAuthorById(
        @PathVariable Integer id
    ) {
        return service.findAuthorById(id)
            .map(author -> ResponseEntity.ok(new AuthorResponse(author)))
            .orElse(ResponseEntity.notFound().build());
    }
}
