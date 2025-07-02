package app.netbooks.backend.services;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import app.netbooks.backend.models.Tag;
import app.netbooks.backend.repositories.interfaces.BooksTagsRepository;

@Service
public class BooksTagsService {
    @Autowired
    private BooksTagsRepository repository;

    public Map<Long, List<Tag>> mapAllByBook() {
        return this.repository.mapAllByBook();
    };

    public List<Tag> findAllByBook(Long id) {
        return this.repository.findAllByBook(id);
    };
};
