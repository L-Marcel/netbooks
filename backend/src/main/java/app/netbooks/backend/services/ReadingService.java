package app.netbooks.backend.services;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import app.netbooks.backend.connections.transactions.Transactions;
import app.netbooks.backend.errors.ReadingAlreadyFinished;
import app.netbooks.backend.errors.ReadingNotFound;
import app.netbooks.backend.errors.TooManyReadings;
import app.netbooks.backend.models.Benefit;
import app.netbooks.backend.models.Reading;
import app.netbooks.backend.repositories.interfaces.ReadingsRepository;

@Service
public class ReadingService {
    @Autowired
    private ReadingsRepository repository;

    @Autowired
    private Transactions transactions;

    public Reading findByUserAndId(UUID user, Long id) {
        return this.repository.findByUserAndId(user, id)
            .orElseThrow(ReadingNotFound::new);
    };

    public Reading findNotFinishedByUserAndId(UUID user, Long id) {
        Reading reading = this.repository.findByUserAndId(user, id)
            .orElseThrow(ReadingNotFound::new);
        
        if(reading.getFinished())
            throw new ReadingAlreadyFinished();

        return reading;
    };

    public Reading findLastByUserAndBook(UUID user, Long book) {
        return this.repository.findLastByUserAndBook(user, book)
            .orElseThrow(ReadingNotFound::new);
    };
    
    public List<Reading> findAllByUser(UUID user) {
        return this.repository.findAllByUser(user);
    };

    public List<Reading> findAllByUserAndBook(UUID user, Long book) {
        return this.repository.findAllByUserAndBook(user, book);
    };

    public Reading start(UUID user, Boolean isAdmin, List<Benefit> benefits, Long book) {
        Integer count = this.repository.countNotFinishedReadingsByUserAndBook(
            user, 
            book
        );

        Boolean hasFiveReadings = isAdmin || benefits.stream().anyMatch(
            (benefit) -> benefit
                .getName()
                .equals("CAN_HAVE_FIVE_READINGS")
        );
        
        if(count >= 5 || (count >= 2 && !hasFiveReadings)) 
            throw new TooManyReadings();

        return this.transactions.run(() -> {
            Reading reading = new Reading(user, book);
            this.repository.create(reading);
            reading = this.findByUserAndId(user, reading.getId());
            return reading;
        });
    };

    public void finishByUserAndId(UUID user, Long id) {
        this.transactions.run(() -> {
            this.findByUserAndId(user, id);
            this.repository.finishById(id);
            this.repository.stopById(id);
        });
    };

    public void updateByUserAndId(UUID user, Long id, Integer page) {
        this.transactions.run(() -> {
            this.findByUserAndId(user, id);
            this.repository.updatePageById(id, page);
            this.repository.stopById(id);
        });
    };
};