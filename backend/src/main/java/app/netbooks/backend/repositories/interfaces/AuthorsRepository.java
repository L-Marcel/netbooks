package app.netbooks.backend.repositories.interfaces;

import java.util.List;
import java.util.Optional;

import app.netbooks.backend.models.Author;

public interface AuthorsRepository {
    public Optional<Author> findById(Long id);
    public List<Author> searchByName(String name);
    public void create(Author author);
    public void createMany(List<Author> authors);
    public void deleteById(Long id);
} ;