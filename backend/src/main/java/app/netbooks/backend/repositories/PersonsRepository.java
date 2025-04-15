package app.netbooks.backend.repositories;

import java.util.List;

import app.netbooks.backend.models.Person;

public interface PersonsRepository {
    public List<Person> findAll();
};
