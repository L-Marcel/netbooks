package app.netbooks.backend.repositories.interfaces;

import java.util.List;
import java.util.Map;

import app.netbooks.backend.models.Book;
import app.netbooks.backend.models.Tag;

public interface BooksTagsRepository {
    public Map<Long, List<Tag>> mapAllByBooks();
    public Map<Long, List<Tag>> mapAllByBooks(List<Book> books);
    public List<Tag> findAllByBook(Long book);
    public void createMany(List<Tag> tags, Long book);
    public void deleteByBook(Long book);
};
