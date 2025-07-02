package app.netbooks.backend.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import app.netbooks.backend.errors.BookNotFound;
import app.netbooks.backend.models.Book;
import app.netbooks.backend.repositories.interfaces.BooksRepository;

@Service
public class BooksService {
    @Autowired
    private BooksRepository repository;
    
    public List<Book> findAll() {
        return this.repository.findAll();
    };

    public Book findById(Long id) {
        return this.repository.findById(id)
            .orElseThrow(BookNotFound::new);
    };
};