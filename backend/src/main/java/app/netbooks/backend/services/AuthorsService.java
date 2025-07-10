package app.netbooks.backend.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import app.netbooks.backend.errors.AuthorNotFound;
import app.netbooks.backend.models.Author;
import app.netbooks.backend.repositories.interfaces.AuthorsRepository;

@Service
public class AuthorsService {
    @Autowired
    private AuthorsRepository repository;

    public List<Author> searchByName(String name) {
        return repository.searchByName(name);
    };
    
    public Author findById(Long id) throws AuthorNotFound {
        return repository.findById(id)
        .orElseThrow(() -> new AuthorNotFound());
    };
};
