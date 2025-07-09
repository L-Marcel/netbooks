package app.netbooks.backend.repositories.interfaces;

import java.util.Optional;
import java.util.UUID;

import app.netbooks.backend.models.Classification;

public interface ClassificationsRepository {
    public Optional<Classification> findByBookAndUser(Long book, UUID user);
    public void createOrUpdate(Classification classification);
};
