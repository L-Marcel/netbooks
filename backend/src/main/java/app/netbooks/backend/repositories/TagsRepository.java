package app.netbooks.backend.repositories;

import java.util.List;

import app.netbooks.backend.models.Tag;

public interface TagsRepository {
    public List<Tag> findAll();
    public void create(Tag tag);
};