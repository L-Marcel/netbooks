package app.netbooks.backend.controllers;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import app.netbooks.backend.annotations.SubscriberOrAdministratorOnly;
import app.netbooks.backend.dtos.response.BookResponse;
import app.netbooks.backend.models.Author;
import app.netbooks.backend.models.Benefit;
import app.netbooks.backend.models.Book;
import app.netbooks.backend.models.Tag;
import app.netbooks.backend.services.BooksAuthorsService;
import app.netbooks.backend.services.BooksBenefitsService;
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
    private BooksBenefitsService booksBenefitsService;

    @GetMapping
    public ResponseEntity<List<BookResponse>> findAll() {
        List<Book> books = this.booksService.findAll();
        Map<Long, List<Author>> mappedAuthors = this.booksAuthorsService.mapAllByBook();
        Map<Long, List<Tag>> mappedTags = this.booksTagsService.mapAllByBook();
        Map<Long, List<Benefit>> mappedRequirements = this.booksBenefitsService.mapAllByBook();

        List<BookResponse> response = BookResponse.fromList(
            books, 
            mappedAuthors, 
            mappedTags, 
            mappedRequirements
        );
        return ResponseEntity.ok().body(response);
    };
    
    @GetMapping("/{id}")
    @SubscriberOrAdministratorOnly
    public ResponseEntity<BookResponse> findById(
        @PathVariable Long id
    ) {
        Book book = this.booksService.findById(id);
        
        List<Author> authors = this.booksAuthorsService.findAllByBook(id);
        List<Tag> tags = this.booksTagsService.findAllByBook(id);
        List<Benefit> requirements = this.booksBenefitsService.findAllByBook(id);

        BookResponse response = new BookResponse(
            book, 
            authors, 
            tags, 
            requirements
        );

        return ResponseEntity.ok().body(response);
    };
};
