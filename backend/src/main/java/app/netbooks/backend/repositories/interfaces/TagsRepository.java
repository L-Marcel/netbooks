package app.netbooks.backend.repositories.interfaces;

import java.util.List;
import java.util.Optional;

import app.netbooks.backend.models.Tag;

public interface TagsRepository {
    public List<Tag> findAll();
    public Optional<Tag> findByName(String name);
    public void create(Tag tag);
    public void deleteByName(String name);
};