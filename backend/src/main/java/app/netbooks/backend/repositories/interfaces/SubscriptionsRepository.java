package app.netbooks.backend.repositories.interfaces;

import java.util.Optional;
import java.util.UUID;

import app.netbooks.backend.models.Subscription;

public interface SubscriptionsRepository {
    public Optional<Subscription> findBySubscriber(UUID subscriber);
    public void create(Subscription subscription);
    public void update(Subscription subscription);
};
