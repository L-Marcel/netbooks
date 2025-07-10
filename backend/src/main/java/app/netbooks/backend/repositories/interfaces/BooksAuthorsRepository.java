package app.netbooks.backend.repositories.interfaces;

import java.util.List;
import java.util.Map;

import app.netbooks.backend.models.Author;

public interface BooksAuthorsRepository {
    public Map<Long, List<Author>> mapAllByBook();
    public List<Author> findAllByBook(Long id);
    public void createMany(List<Author> authors, Long book);
};
