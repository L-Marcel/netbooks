package app.netbooks.backend.services;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import app.netbooks.backend.errors.ClassificationNotFound;
import app.netbooks.backend.models.Classification;
import app.netbooks.backend.repositories.interfaces.ClassificationsRepository;
import app.netbooks.backend.validation.Validator;

@Service
public class ClassificationsService {
    @Autowired
    private ClassificationsRepository repository;

    public Classification findByBookAndUser(Long book, UUID user) {
        return this.repository.findByBookAndUser(book, user)
            .orElseThrow(ClassificationNotFound::new);
    };

    public Classification createOrUpdate(Long book, UUID user, Integer value) {
        Validator validator = new Validator();

        validator.validate("value", value)
            .min(1, "O valor é maior que 0!", "O valor deve ser maior que 0!")
            .max(10, "O valor é menor que 11!", "O valor deve ser menor que 11!");

        validator.run();

        Classification classification = new Classification(book, user, value);
        this.repository.createOrUpdate(classification);
        return classification;
    };
};
