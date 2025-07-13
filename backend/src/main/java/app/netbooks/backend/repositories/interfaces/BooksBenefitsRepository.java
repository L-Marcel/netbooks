package app.netbooks.backend.repositories.interfaces;

import java.util.List;
import java.util.Map;

import app.netbooks.backend.models.Benefit;
import app.netbooks.backend.models.Book;


public interface BooksBenefitsRepository {
    public Map<Long, List<Benefit>> mapAllByBooks();
    public Map<Long, List<Benefit>> mapAllByBooks(List<Book> book);
    public List<Benefit> findAllByBook(Long id);
    public void createMany(List<Benefit> benefits, Long book);
    public void deleteByBook(Long book);
};
