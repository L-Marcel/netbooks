package app.netbooks.backend.repositories;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import app.netbooks.backend.models.PlanEdition;

public interface PlanEditionsRepository {
    public List<PlanEdition> getCurrentAvailable();
    public Optional<PlanEdition> findById(UUID uuid);
    public void create(PlanEdition plan);
    public void update(PlanEdition plan);
    public void close(PlanEdition plan);
};
