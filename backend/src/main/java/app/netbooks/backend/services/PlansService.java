package app.netbooks.backend.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import app.netbooks.backend.errors.PlanNotFound;
import app.netbooks.backend.models.Plan;
import app.netbooks.backend.repositories.interfaces.PlansRepository;

@Service
public class PlansService {
    @Autowired
    private PlansRepository repository;

    public List<Plan> findAll() {
        return this.repository.findAll();
    };

    public List<Plan> findAllAvailable() {
        return this.repository.findAllAvailable();
    };

    public Plan findById(Integer id) throws PlanNotFound {
        return this.repository.findById(id)
            .orElseThrow(PlanNotFound::new);
    };
};
