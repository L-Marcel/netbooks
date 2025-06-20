package app.netbooks.backend.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import app.netbooks.backend.dtos.response.AuthorResponse;
import app.netbooks.backend.models.Author;
import app.netbooks.backend.services.AuthorsService;


@RestController
@RequestMapping("/authors")
public class AuthorsController {
    @Autowired
    private AuthorsService service;

    @GetMapping
    public ResponseEntity<List<AuthorResponse>> getAuthorsByName(
        @RequestParam(required = false, defaultValue = "") String name
    ) {
        List<Author> authors = service.searchAuthorsByName(name);
        List<AuthorResponse> response = AuthorResponse.fromList(authors);
        
        return ResponseEntity.ok().body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AuthorResponse> getAuthorById(
        @PathVariable Integer id
    ) {
        Author author = service.findAuthorById(id);
        AuthorResponse response = new AuthorResponse(author);

        return ResponseEntity.ok().body(response);
    }
}
