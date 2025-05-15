package app.netbooks.backend.repositories;

import java.util.List;
import java.util.Optional;
import app.netbooks.backend.models.Plan;

public interface PlansRepository {
    public List<Plan> findAll();
    public Optional<Plan> findById(Integer id);
    public void create(Plan plan);
    public void update(Plan plan);
};
