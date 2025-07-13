package app.netbooks.backend.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import app.netbooks.backend.errors.PublisherNotFound;
import app.netbooks.backend.models.Publisher;
import app.netbooks.backend.repositories.interfaces.PublishersRepository;

@Service
public class PublisherService {
    @Autowired
    private PublishersRepository repository;

    public List<Publisher> findAll() {
        return this.repository.findAll();
    };

    public Publisher findByName(String name) {
        return this.repository.findByName(name)
            .orElseThrow(
                () -> new PublisherNotFound()
            );
    };

    public List<Publisher> searchByName(String name) {
        return this.repository.searchByName(name);
    };

    public void create(Publisher publisher) {
        this.repository.create(publisher);
    };

    public void delete(String name) {
        this.repository.deleteByName(name);
    };
};
