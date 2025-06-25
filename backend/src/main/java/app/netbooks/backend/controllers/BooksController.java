package app.netbooks.backend.controllers;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import app.netbooks.backend.dtos.response.BookResponse;
import app.netbooks.backend.models.Author;
import app.netbooks.backend.models.Benefit;
import app.netbooks.backend.models.Book;
import app.netbooks.backend.models.Tag;
import app.netbooks.backend.repositories.interfaces.BooksBenefitsRepository;
import app.netbooks.backend.services.BooksAuthorsService;
import app.netbooks.backend.services.BooksService;
import app.netbooks.backend.services.BooksTagsService;

@RestController
@RequestMapping("/books")
public class BooksController {
    @Autowired
    private BooksService booksService;

    @Autowired
    private BooksAuthorsService booksAuthorsService;

    @Autowired
    private BooksTagsService booksTagsService;

    @Autowired
    private BooksBenefitsRepository booksBenefitsRepository;

    @GetMapping
    public ResponseEntity<List<BookResponse>> findAll() {
        List<Book> books = booksService.findAll();
        Map<Long, List<Author>> mappedAuthors = booksAuthorsService.mapAllByBook();
        Map<Long, List<Tag>> mappedTags = booksTagsService.mapAllByBook();
        Map<Long, List<Benefit>> mappedRequirements = booksBenefitsRepository.mapAllByBook();

        List<BookResponse> response = BookResponse.fromList(
            books, 
            mappedAuthors, 
            mappedTags, 
            mappedRequirements
        );
        return ResponseEntity.ok().body(response);
    };
};
