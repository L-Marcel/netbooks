package app.netbooks.backend.repositories;

import java.util.List;
import java.util.Optional;

import app.netbooks.backend.models.Publisher;

public interface PublisherRepository {
    public List<Publisher> findAll();
    public Optional<Publisher> find(String name);
    public void create(Publisher publisher);
    public void deleteByName(String name);
}
