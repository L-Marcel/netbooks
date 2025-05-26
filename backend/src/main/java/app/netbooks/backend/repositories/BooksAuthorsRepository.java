package app.netbooks.backend.repositories;

import java.util.List;
import java.util.Map;

import app.netbooks.backend.models.Author;

public interface BooksAuthorsRepository {
    public Map<Long, List<Author>> mapAllBookAuthors();
    public List<Author> findAuthors(Long id);
};
