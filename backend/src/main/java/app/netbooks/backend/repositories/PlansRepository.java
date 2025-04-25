package app.netbooks.backend.repositories;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import app.netbooks.backend.models.Plan;

public interface PlansRepository {
    public List<Plan> findAll();
    public Optional<Plan> findById(UUID uuid);
    public void create(Plan plan);
    public void update(Plan plan);
};
