package app.netbooks.backend.repositories;

import java.util.Optional;
import java.util.UUID;

import app.netbooks.backend.models.Plan;

public interface PlansRepository {
    public Optional<Plan> findById(UUID uuid);
    public void create(Plan plan);
    public void update(Plan plan);
};
