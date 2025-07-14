package app.netbooks.backend.repositories.interfaces;

import java.util.List;
import java.util.Optional;

import app.netbooks.backend.models.Book;
import app.netbooks.backend.models.User;

public interface BooksRepository {
    public List<Book> findAll();
    public Optional<Book> findById(Long id);
    public Optional<Book> findByIsbn(Long isbn);
    public void create(Book book);
    public void deleteById(Long id);
    public void update(Book book);
    public List<Book> findBooksWithAllTags(List<String> tagNames, int size);
    public List<Book> search(String query);
    public List<Book> searchFromBookcase(String query, User user);
};
