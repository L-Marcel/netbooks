package app.netbooks.backend.controllers;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import app.netbooks.backend.annotations.AdministratorOnly;
import app.netbooks.backend.annotations.AuthenticatedOnly;
import app.netbooks.backend.annotations.SubscriberOrAdministratorOnly;
import app.netbooks.backend.authentication.AuthenticatedUser;
import app.netbooks.backend.dtos.request.ClassificationRequestBody;
import app.netbooks.backend.dtos.request.RegisterBookRequestBody;
import app.netbooks.backend.dtos.request.UpdateBookRequestBody;
import app.netbooks.backend.dtos.response.BookResponse;
import app.netbooks.backend.dtos.response.ClassificationResponse;
import app.netbooks.backend.models.Author;
import app.netbooks.backend.models.Benefit;
import app.netbooks.backend.models.Book;
import app.netbooks.backend.models.Classification;
import app.netbooks.backend.models.Tag;
import app.netbooks.backend.services.BooksAuthorsService;
import app.netbooks.backend.services.BooksBenefitsService;
import app.netbooks.backend.services.BooksService;
import app.netbooks.backend.services.BooksTagsService;
import app.netbooks.backend.services.ClassificationsService;
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

    @Autowired
    private ClassificationsService classificationsService;

    @GetMapping
    public ResponseEntity<List<BookResponse>> findAll() {
        List<Book> books = this.booksService.findAll();
        Map<Long, List<Author>> mappedAuthors = this.booksAuthorsService.mapAllByBooks();
        Map<Long, List<Tag>> mappedTags = this.booksTagsService.mapAllByBooks();
        Map<Long, List<Benefit>> mappedRequirements = this.booksBenefitsService.mapAllByBooks();

        List<BookResponse> response = BookResponse.fromList(
            books, 
            mappedAuthors, 
            mappedTags, 
            mappedRequirements
        );
        return ResponseEntity.ok().body(response);
    };

    @GetMapping("/search")
    public ResponseEntity<List<BookResponse>> search(
        @RequestParam(required = false, defaultValue = "") String query
    ) {
        List<Book> books = this.booksService.search(query);
        Map<Long, List<Author>> mappedAuthors = this.booksAuthorsService.mapAllByBooks(books);
        Map<Long, List<Tag>> mappedTags = this.booksTagsService.mapAllByBooks(books);
        Map<Long, List<Benefit>> mappedRequirements = this.booksBenefitsService.mapAllByBooks(books);

        List<BookResponse> response = BookResponse.fromList(
            books, 
            mappedAuthors, 
            mappedTags, 
            mappedRequirements
        );
        return ResponseEntity.ok().body(response);
    };

    @AuthenticatedOnly
    @GetMapping("/bookcase/search")
    public ResponseEntity<List<BookResponse>> searchFromBookcase(
        @AuthenticationPrincipal AuthenticatedUser user,
        @RequestParam(required = false, defaultValue = "") String query
    ) {
        List<Book> books = this.booksService.searchFromBookcase(query, user.getUser());
        Map<Long, List<Author>> mappedAuthors = this.booksAuthorsService.mapAllByBooks(books);
        Map<Long, List<Tag>> mappedTags = this.booksTagsService.mapAllByBooks(books);
        Map<Long, List<Benefit>> mappedRequirements = this.booksBenefitsService.mapAllByBooks(books);

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

    @GetMapping("/{book}/classification")
    @AuthenticatedOnly
    public ResponseEntity<ClassificationResponse> findClassificationByBookAndUser(
        @PathVariable Long book,
        @AuthenticationPrincipal AuthenticatedUser user
    ) {
        Classification classification = this.classificationsService.findByBookAndUser(
            book, 
            user.getUser().getUuid()
        );

        ClassificationResponse response = new ClassificationResponse(classification);
        return ResponseEntity.ok().body(response);
    };

    @PostMapping("/{book}/classification")
    @AuthenticatedOnly
    public ResponseEntity<ClassificationResponse> createOrUpdateClassificationByBookAndUser(
        @PathVariable Long book,
        @AuthenticationPrincipal AuthenticatedUser user,
        @RequestBody ClassificationRequestBody body
    ) {
        Classification classification = this.classificationsService.createOrUpdate(
            book, 
            user.getUser().getUuid(),
            body.getValue()
        );

        ClassificationResponse response = new ClassificationResponse(classification);
        return ResponseEntity.ok().body(response);
    };

    @GetMapping("/{id}/download")
    @SubscriberOrAdministratorOnly
    public ResponseEntity<Resource> findContentById(
        @AuthenticationPrincipal AuthenticatedUser user,
        @PathVariable Long id,
        @RequestParam(defaultValue = "1") Integer page
    ) {
        Book book = this.booksService.findById(id);
        
        List<Benefit> benefits = this.plansBenefitsService.findAllBySubscriber(
            user.getUser().getUuid()
        );

        this.booksBenefitsService.validateBookAccessToDownlaod(
            book.getId(), 
            benefits,
            user.isAdmin()
        );

        Resource content = this.booksService.findContentById(
            book.getId()
        );

        String contentDisposition = String.format(
            "inline; filename=\"%s.pdf\"",
            book.getTitle()
        );

        return ResponseEntity.ok()
            .header(HttpHeaders.CONTENT_DISPOSITION, contentDisposition)
            .header("Filename", book.getTitle() + ".pdf")
            .contentType(MediaType.APPLICATION_PDF)
            .body(content);
    };

    @PostMapping
    @AdministratorOnly
    public ResponseEntity<Void> create(
        @RequestPart("body") RegisterBookRequestBody body,
        @RequestPart("cover") MultipartFile cover,
        @RequestPart("banner") MultipartFile banner,
        @RequestPart("file") MultipartFile file
    ) {
        this.booksService.create(
            body.getTitle(),
            body.getDescription(),
            body.getIsbn(),
            body.getPublishedIn(),
            body.getPublisher(),
            body.getTags(),
            body.getAuthors(),
            body.getRequirements(),
            cover,
            banner,
            file
        );

        return ResponseEntity.status(HttpStatus.CREATED).build();
    };

    @PutMapping("/{id}")
    @AdministratorOnly
    public ResponseEntity<Void> update(
        @RequestPart("body") UpdateBookRequestBody body,
        @RequestPart("cover") MultipartFile cover,
        @RequestPart("banner") MultipartFile banner,
        @RequestPart("file") MultipartFile file,
        @PathVariable Long id
    ) {
        Book book = this.booksService.findById(id);
        this.booksService.update(
            book,
            body.getTitle(),
            body.getDescription(),
            body.getIsbn(),
            body.getPublishedIn(),
            body.getPublisher(),
            body.getTags(),
            body.getAuthors(),
            body.getRequirements(),
            cover,
            banner,
            file
        );

        return ResponseEntity.status(HttpStatus.CREATED).build();
    };

    @DeleteMapping("/{id}")
    @AdministratorOnly
    public ResponseEntity<Void> delete(
        @PathVariable Long id
    ) {
        this.booksService.deleteById(id);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    };
};
