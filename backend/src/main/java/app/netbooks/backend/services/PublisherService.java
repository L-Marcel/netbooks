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
        return repository.findAll();
    }

    public Publisher findByName(String name) {
        return repository.findByName(name)
            .orElseThrow(
                () -> new PublisherNotFound()
            );
    }

    public void create(Publisher publisher) {
        repository.create(publisher);
    }

    public void delete(String name) {
        repository.deleteByName(name);
    }
}
