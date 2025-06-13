package app.netbooks.backend.services;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import app.netbooks.backend.errors.SubscriptionNotFound;
import app.netbooks.backend.models.PlanEdition;
import app.netbooks.backend.models.Subscription;
import app.netbooks.backend.models.User;
import app.netbooks.backend.repositories.interfaces.SubscriptionsRepository;

@Service
public class SubscriptionsService {
    @Autowired
    private SubscriptionsRepository repository;

    public Subscription findBySubscriber(UUID subscriber) {
        return this.repository.findBySubscriber(subscriber)
            .orElseThrow(SubscriptionNotFound::new);
    };

    public void subscribe(
        User subscriber, 
        PlanEdition edition
    ) {
        this.repository.subscribe(subscriber.getUuid(), edition.getId());
    };

    public void unsubscribe(User subscriber) {
        this.repository.unsubscribe(subscriber.getUuid());
    };
};
