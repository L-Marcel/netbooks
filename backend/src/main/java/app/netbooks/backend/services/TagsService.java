package app.netbooks.backend.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import app.netbooks.backend.models.Tag;
import app.netbooks.backend.repositories.TagsRepository;

@Service
public class TagsService {
    @Autowired
    private TagsRepository repository;

    public List<Tag> findAll() {
        return this.repository.findAll();
    }

};
