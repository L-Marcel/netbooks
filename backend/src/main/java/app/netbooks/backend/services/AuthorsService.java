package app.netbooks.backend.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import app.netbooks.backend.models.Author;
import app.netbooks.backend.repositories.AuthorRepository;

@Service
public class AuthorsService {
    @Autowired
    private AuthorRepository repository;

    public List<Author> findAuthorsByName(String name) {
        return repository.findByName(name);
    }

    public Optional<Author> findAuthorById(Integer id) {
        return repository.findById(id);
    }
}
