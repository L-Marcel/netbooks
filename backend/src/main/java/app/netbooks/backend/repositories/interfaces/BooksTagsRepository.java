package app.netbooks.backend.repositories.interfaces;

import java.util.List;
import java.util.Map;

import app.netbooks.backend.models.Tag;

public interface BooksTagsRepository {
    public Map<Long, List<Tag>> mapAllByBook();
    public List<Tag> findAllByBook(Long id);
};
