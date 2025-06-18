package app.netbooks.backend.repositories.interfaces;

import java.util.Optional;
import java.util.UUID;

import app.netbooks.backend.models.Subscription;

public interface SubscriptionsRepository {
    public Optional<Subscription> findBySubscriber(UUID subscriber);
    public void unsubscribe(UUID subscriber);
    public Long subscribe(UUID subscriber, Integer edition);
    public void createSubscriberIfNotExists(UUID subscriber);
    public void closeOldSubscriptions(UUID subscriber);
    public void upgrade(UUID subscriber, Long subscription, Integer newEdition);
    public void downgrade(UUID subscriber, Long subscription, Integer newEdition);
};
