package app.netbooks.backend.services;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import app.netbooks.backend.models.Author;
import app.netbooks.backend.models.Book;
import app.netbooks.backend.repositories.interfaces.BooksAuthorsRepository;

@Service
public class BooksAuthorsService {
    @Autowired
    private BooksAuthorsRepository repository;

    public Map<Long, List<Author>> mapAllByBooks() {
        return this.repository.mapAllByBooks();
    };

    public Map<Long, List<Author>> mapAllByBooks(List<Book> books) {
        return this.repository.mapAllByBooks(books);
    };

    public List<Author> findAllByBook(Long id) {
        return this.repository.findAllByBook(id);
    };
};
