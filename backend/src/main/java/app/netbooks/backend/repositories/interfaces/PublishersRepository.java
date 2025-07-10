package app.netbooks.backend.repositories.interfaces;

import java.util.List;
import java.util.Optional;

import app.netbooks.backend.models.Publisher;

public interface PublishersRepository {
    public List<Publisher> findAll();
    public List<Publisher> searchByName(String name);
    public Optional<Publisher> findByName(String name);
    public void create(Publisher publisher);
    public void deleteByName(String name);
}
