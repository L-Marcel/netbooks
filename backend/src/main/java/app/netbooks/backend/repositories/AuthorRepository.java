package app.netbooks.backend.repositories;

import java.util.List;
import java.util.Optional;
import app.netbooks.backend.models.Author;

public interface AuthorRepository {
    public Optional<Author> findById(Integer id);
    public List<Author> findByName(String name);
    public void create(Author author);
    public void deleteById(Integer id);
} 