package app.netbooks.backend.controllers;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import app.netbooks.backend.annotations.SubscriberOrAdministratorOnly;
import app.netbooks.backend.authentication.AuthenticatedUser;
import app.netbooks.backend.dtos.response.BookResponse;
import app.netbooks.backend.models.Author;
import app.netbooks.backend.models.Benefit;
import app.netbooks.backend.models.Book;
import app.netbooks.backend.models.Tag;
import app.netbooks.backend.services.BooksAuthorsService;
import app.netbooks.backend.services.BooksBenefitsService;
import app.netbooks.backend.services.BooksService;
import app.netbooks.backend.services.BooksTagsService;
import app.netbooks.backend.services.PlansBenefitsService;

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

    @Autowired
    private PlansBenefitsService plansBenefitsService;


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

    @GetMapping("/{id}/content")
    @SubscriberOrAdministratorOnly
    public ResponseEntity<Resource> findContentById(
        @AuthenticationPrincipal AuthenticatedUser user,
        @PathVariable Long id
    ) {
        Book book = this.booksService.findById(id);
        
        List<Benefit> benefits = this.plansBenefitsService.findAllBySubscriber(
            user.getUser().getUuid()
        );

        List<Benefit> requirements = this.booksBenefitsService.findAllByBook(
            book.getId()
        );

        Resource content = this.booksService.findContentById(
            book.getId()
        );

        return ResponseEntity.ok().body(content);
    };
};
