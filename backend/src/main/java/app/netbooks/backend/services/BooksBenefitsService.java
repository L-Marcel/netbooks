package app.netbooks.backend.services;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import app.netbooks.backend.errors.Unauthorized;
import app.netbooks.backend.models.Benefit;
import app.netbooks.backend.models.Book;
import app.netbooks.backend.repositories.interfaces.BooksBenefitsRepository;

@Service
public class BooksBenefitsService {
    @Autowired
    private BooksBenefitsRepository repository;

    public Map<Long, List<Benefit>> mapAllByBooks() {
        return this.repository.mapAllByBooks();
    };

    public Map<Long, List<Benefit>> mapAllByBooks(List<Book> books) {
        return this.repository.mapAllByBooks(books);
    };

    public List<Benefit> findAllByBook(Long id) {
        return this.repository.findAllByBook(id);
    };

    public void validateBookAccess(Long id, List<Benefit> benefits, Boolean isAdmin) {
        List<Benefit> requirements = this.findAllByBook(
            id
        );

        if(!isAdmin && !requirements.stream().allMatch(
            (requirement) -> {
                return benefits.stream().anyMatch(
                    (benefit) -> benefit.getName().equals(requirement.getName())
                );
            }
        )) throw new Unauthorized();
    };

    public void validateBookAccessToDownlaod(Long id, List<Benefit> benefits, Boolean isAdmin) {
        this.validateBookAccess(id, benefits, isAdmin);

        if(
            !isAdmin && !benefits.stream().anyMatch(
                (benefit) -> benefit.getName().equals("CAN_DOWNLOAD_BOOKS")
            )
        ) throw new Unauthorized();
    };
};
