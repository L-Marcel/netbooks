package app.netbooks.backend.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import app.netbooks.backend.annotations.SubscriberOrAdministratorOnly;
import app.netbooks.backend.authentication.AuthenticatedUser;
import app.netbooks.backend.dtos.response.ReadingResponse;
import app.netbooks.backend.models.Benefit;
import app.netbooks.backend.models.Book;
import app.netbooks.backend.models.Reading;
import app.netbooks.backend.services.BooksBenefitsService;
import app.netbooks.backend.services.BooksService;
import app.netbooks.backend.services.PlansBenefitsService;
import app.netbooks.backend.services.ReadingService;

@RestController
@RequestMapping("/readings")
public class ReadingsController {
    @Autowired
    private ReadingService readingsService;

    @Autowired
    private PlansBenefitsService plansBenefitsService;
    
    @Autowired
    private BooksBenefitsService booksBenefitsService;

    @Autowired
    private BooksService booksService;

    @GetMapping("/{id}")
    @SubscriberOrAdministratorOnly
    public ResponseEntity<ReadingResponse> findById(
        @AuthenticationPrincipal AuthenticatedUser user,
        @PathVariable Long id
    ) {
        Reading reading = this.readingsService.findByUserAndId(
            user.getUser().getUuid(),
            id
        );

        ReadingResponse response = new ReadingResponse(
            reading
        );

        return ResponseEntity.ok().body(response);
    };

    @GetMapping("/{id}/content")
    @SubscriberOrAdministratorOnly
    public ResponseEntity<Resource> findContentById(
        @AuthenticationPrincipal AuthenticatedUser user,
        @PathVariable Long id,
        @RequestParam(defaultValue = "1") Integer page
    ) {
        Reading reading = this.readingsService.findNotFinishedByUserAndId(
            user.getUser().getUuid(),
            id
        );

        Book book = this.booksService.findById(reading.getBook());
        
        List<Benefit> benefits = this.plansBenefitsService.findAllBySubscriber(
            user.getUser().getUuid()
        );

        this.booksBenefitsService.validateBookAccess(
            book.getId(), 
            benefits
        );

        Resource content = this.booksService.findContentById(
            book.getId(),
            --page
        );

        this.readingsService.updateByUserAndId(
            user.getUser().getUuid(), 
            id, 
            ++page
        );

        String contentDisposition = String.format(
            "inline; filename=\"%s_page_%d.pdf\"",
            book.getTitle(),
            page
        );

        return ResponseEntity.ok()
            .header(HttpHeaders.CONTENT_DISPOSITION, contentDisposition)
            .header("Filename", book.getTitle() + ".pdf")
            .contentType(MediaType.APPLICATION_PDF)
            .body(content);
    };

    @GetMapping("/books/{book}")
    @SubscriberOrAdministratorOnly
    public ResponseEntity<ReadingResponse> findLastByUserAndBook(
        @AuthenticationPrincipal AuthenticatedUser user,
        @PathVariable Long book
    ) {
        Reading reading = this.readingsService.findLastByUserAndBook(
            user.getUser().getUuid(),
            book
        );

        ReadingResponse response = new ReadingResponse(
            reading
        );

        return ResponseEntity.ok().body(response);
    };

    @PostMapping("/{id}/finish")
    @SubscriberOrAdministratorOnly
    public ResponseEntity<Void> finishByUserAndId(
        @AuthenticationPrincipal AuthenticatedUser user,
        @PathVariable Long id
    ) {
        this.readingsService.finishByUserAndId(
            user.getUser().getUuid(),
            id
        );

        return ResponseEntity.ok().build();
    };

    @GetMapping("/me")
    @SubscriberOrAdministratorOnly
    public ResponseEntity<List<ReadingResponse>> findAllByUser(
        @AuthenticationPrincipal AuthenticatedUser user
    ) {
        List<Reading> readings = this.readingsService.findAllByUser(
            user.getUser().getUuid()
        );

        List<ReadingResponse> response = ReadingResponse.fromList(readings);
        return ResponseEntity.ok().body(response);
    };

    @GetMapping("/me/{book}")
    @SubscriberOrAdministratorOnly
    public ResponseEntity<List<ReadingResponse>> findAllByUser(
        @AuthenticationPrincipal AuthenticatedUser user,
        @PathVariable Long book
    ) {
        List<Reading> readings = this.readingsService.findAllByUserAndBook(
            user.getUser().getUuid(),
            book
        );

        List<ReadingResponse> response = ReadingResponse.fromList(readings);
        return ResponseEntity.ok().body(response);
    };

    @PostMapping("/me/{book}")
    @SubscriberOrAdministratorOnly
    public ResponseEntity<ReadingResponse> start(
        @AuthenticationPrincipal AuthenticatedUser user,
        @PathVariable Long book
    ) {
        List<Benefit> benefits = this.plansBenefitsService.findAllBySubscriber(
            user.getUser().getUuid()
        );

        Reading reading = this.readingsService.start(
            user.getUser().getUuid(),
            user.isAdmin(),
            benefits,
            book
        );

        ReadingResponse response = new ReadingResponse(
            reading
        );

        return ResponseEntity.status(HttpStatus.CREATED)
            .body(response);
    };
};
