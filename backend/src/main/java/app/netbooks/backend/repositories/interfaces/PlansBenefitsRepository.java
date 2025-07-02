package app.netbooks.backend.repositories.interfaces;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import app.netbooks.backend.models.Benefit;

public interface PlansBenefitsRepository {
    public Map<Integer, List<Benefit>> mapAllByPlan();
    public Map<Integer, List<Benefit>> mapAllAvailableByPlan();
    public List<Benefit> findAllByPlan(Integer plan);
    public List<Benefit> findAllBySubscriber(UUID subscriber);
};