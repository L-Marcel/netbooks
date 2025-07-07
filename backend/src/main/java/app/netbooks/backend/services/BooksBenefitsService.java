package app.netbooks.backend.services;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import app.netbooks.backend.errors.Unauthorized;
import app.netbooks.backend.models.Benefit;
import app.netbooks.backend.repositories.interfaces.BooksBenefitsRepository;

@Service
public class BooksBenefitsService {
    @Autowired
    private BooksBenefitsRepository repository;

    public Map<Long, List<Benefit>> mapAllByBook() {
        return this.repository.mapAllByBook();
    };

    public List<Benefit> findAllByBook(Long id) {
        return this.repository.findAllByBook(id);
    };

    public void validateBookAccess(Long id, List<Benefit> benefits) {
        List<Benefit> requirements = this.findAllByBook(
            id
        );
        
        System.out.println(requirements);
        System.out.println(benefits);

        if(!requirements.stream().allMatch(
            (requirement) -> {
                return benefits.stream().anyMatch(
                    (benefit) -> benefit.getName().equals(requirement.getName())
                );
            }
        )) throw new Unauthorized();
    };
};
