package app.netbooks.backend.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import app.netbooks.backend.dtos.BookResponse;
import app.netbooks.backend.models.Book;
import app.netbooks.backend.services.BooksService;

@RestController
@RequestMapping("/books")
public class BooksController {
    @Autowired
    private BooksService service;

    @GetMapping
    public ResponseEntity<List<BookResponse>> get() {
        List<Book> books = service.findAll();
        List<BookResponse> response = BookResponse.fromList(books);
        return ResponseEntity.ok().body(response);
    };
};
