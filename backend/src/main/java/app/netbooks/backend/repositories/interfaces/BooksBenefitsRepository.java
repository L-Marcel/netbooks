package app.netbooks.backend.repositories.interfaces;

import java.util.List;
import java.util.Map;

import app.netbooks.backend.models.Benefit;

public interface BooksBenefitsRepository {
    public Map<Long, List<Benefit>> mapAllByBook();
    public List<Benefit> findAllByBook(Long id);
};
