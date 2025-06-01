package app.netbooks.backend.repositories.interfaces;

import java.util.List;
import java.util.Map;

import app.netbooks.backend.models.PlanEdition;

public interface PlansEditionsRepository {
    public List<PlanEdition> findAll();
    public Map<Integer, List<PlanEdition>> mapAllByPlan();
    public Map<Integer, List<PlanEdition>> mapAllAvailableByPlan();
    // public List<PlanEdition> getCurrentAvailable();
    // public Optional<PlanEdition> findById(Integer id);
    // public void create(PlanEdition plan);
    // public void update(PlanEdition plan);
    // public void close(PlanEdition plan);
};
