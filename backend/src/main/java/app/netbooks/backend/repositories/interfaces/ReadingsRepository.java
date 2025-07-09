package app.netbooks.backend.repositories.interfaces;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import app.netbooks.backend.models.Reading;

public interface ReadingsRepository {
    public List<Reading> findAllByUser(UUID user);
    public List<Reading> findAllByUserAndBook(UUID user, Long book);
    public Optional<Reading> findByUserAndId(UUID user, Long id);
    public Optional<Reading> findLastByUserAndBook(UUID user, Long book);
    public Integer countNotFinishedReadingsByUserAndBook(UUID user, Long book);
    public void create(Reading reading);
    public void updatePageById(Long id, Integer page);
    public void finishById(Long id);
    public void stopById(Long id);
};
