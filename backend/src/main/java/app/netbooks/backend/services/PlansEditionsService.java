package app.netbooks.backend.services;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import app.netbooks.backend.models.PlanEdition;
import app.netbooks.backend.repositories.interfaces.PlansEditionsRepository;

@Service
public class PlansEditionsService {
    @Autowired
    private PlansEditionsRepository repository;

    public List<PlanEdition> findAll() {
        return this.repository.findAll();
    };
    
    public Map<Integer, List<PlanEdition>> mapAllByPlan() {
        return this.repository.mapAllByPlan();
    };

    public Map<Integer, List<PlanEdition>> mapAllAvailableByPlan() {
        return this.repository.mapAllAvailableByPlan();
    };
};
