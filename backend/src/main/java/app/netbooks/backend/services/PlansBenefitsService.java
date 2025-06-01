package app.netbooks.backend.services;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import app.netbooks.backend.models.Benefit;
import app.netbooks.backend.repositories.interfaces.PlansBenefitsRepository;

@Service
public class PlansBenefitsService {
    @Autowired
    private PlansBenefitsRepository repository;

    public Map<Integer, List<Benefit>> mapAllByPlan() {
        return this.repository.mapAllByPlan();
    };

    public Map<Integer, List<Benefit>> mapAllAvailableByPlan() {
        return this.repository.mapAllAvailableByPlan();
    };

    public List<Benefit> findByPlan(Integer plan) {
        return this.repository.findAllByPlan(plan);
    };
};
