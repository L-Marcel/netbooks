package app.netbooks.backend.repositories;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import app.netbooks.backend.models.Author;

public interface AuthorRepository {
    public Optional<Author> findById(int id);
    public List<Author> findByName(String name);
    public void create(Author author);
    public void deleteById(int id);
} 