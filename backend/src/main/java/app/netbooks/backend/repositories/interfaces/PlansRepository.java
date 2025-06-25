package app.netbooks.backend.repositories.interfaces;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import app.netbooks.backend.models.Plan;

public interface PlansRepository {
    public List<Plan> findAll();
    public List<Plan> findAllAvailable();
    public Optional<Plan> findById(Integer id);
    public Map<Integer, Integer> compareBenefitsById(Integer a, Integer b);
    public void create(Plan plan);
    public void update(Plan plan);
};
