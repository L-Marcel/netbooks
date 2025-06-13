package app.netbooks.backend.repositories.interfaces;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import app.netbooks.backend.models.PlanEdition;

public interface PlansEditionsRepository {
    public List<PlanEdition> findAll();
    public Map<Integer, List<PlanEdition>> mapAllByPlan();
    public Map<Integer, List<PlanEdition>> mapAllAvailableByPlan();
    public Optional<PlanEdition> findById(Integer id);
};
