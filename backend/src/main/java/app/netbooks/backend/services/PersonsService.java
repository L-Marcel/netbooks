package app.netbooks.backend.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import app.netbooks.backend.models.Person;
import app.netbooks.backend.repositories.PersonsRepository;

@Service
public class PersonsService {
    @Autowired
    private PersonsRepository repository;

    public List<Person> findAll() {
        return this.repository.findAll();
    }; 
};
