package app.netbooks.backend.repositories.interfaces;

import java.util.List;
import java.util.Map;

import app.netbooks.backend.models.Author;
import app.netbooks.backend.models.Book;

public interface BooksAuthorsRepository {
    public Map<Long, List<Author>> mapAllByBooks();
    public Map<Long, List<Author>> mapAllByBooks(List<Book> list);
    public List<Author> findAllByBook(Long id);
    public void createMany(List<Author> authors, Long book);
    public void deleteByBook(Long book);
};
