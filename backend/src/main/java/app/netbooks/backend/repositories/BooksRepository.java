package app.netbooks.backend.repositories;

import java.util.List;
import java.util.Optional;

import app.netbooks.backend.models.Book;

public interface BooksRepository {
    public List<Book> findAll();
    public Optional<Book> findById(Long id);
};
