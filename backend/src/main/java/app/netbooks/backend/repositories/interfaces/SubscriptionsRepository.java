package app.netbooks.backend.repositories.interfaces;

import java.sql.Date;
import java.util.Optional;
import java.util.UUID;

import app.netbooks.backend.models.Subscription;

public interface SubscriptionsRepository {
    public Optional<Subscription> findBySubscriber(UUID subscriber);
    public Optional<Subscription> findById(Long id);
    public Long subscribe(Subscription subscription);
    public void closeById(Long id);
    public void closeById(Long id, Date closeDate);
    public void closeNotClosedBySubscriber(UUID subscriber);
};