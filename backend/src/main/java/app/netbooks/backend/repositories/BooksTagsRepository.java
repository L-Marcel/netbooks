package app.netbooks.backend.repositories;

import java.util.List;
import java.util.Map;

import app.netbooks.backend.models.Tag;

public interface BooksTagsRepository {
    public Map<Long, List<Tag>> mapAllBookTags();
    public List<Tag> findTags(Long id);
};
