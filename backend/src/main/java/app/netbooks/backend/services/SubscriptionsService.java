package app.netbooks.backend.services;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import app.netbooks.backend.authentication.AuthenticatedUser;
import app.netbooks.backend.errors.SubscriptionAlreadyExists;
import app.netbooks.backend.errors.SubscriptionNotFound;
import app.netbooks.backend.models.PlanEdition;
import app.netbooks.backend.models.Subscription;
import app.netbooks.backend.models.User;
import app.netbooks.backend.repositories.interfaces.PlansRepository;
import app.netbooks.backend.repositories.interfaces.SubscriptionsRepository;

@Service
public class SubscriptionsService {
    @Autowired
    private SubscriptionsRepository subscriptionsRepository;

    @Autowired
    private PlansRepository plansRepository;

    public Subscription findBySubscriber(UUID subscriber) throws SubscriptionNotFound {
        return this.subscriptionsRepository.findBySubscriber(subscriber)
            .orElseThrow(SubscriptionNotFound::new);
    };

    public Optional<Subscription> searchBySubscriber(UUID subscriber) {
        return this.subscriptionsRepository.findBySubscriber(subscriber);
    };

    public void subscribe(
        AuthenticatedUser subscriber, 
        PlanEdition edition
    ) throws SubscriptionAlreadyExists {
        User user = subscriber.getUser();

        if(subscriber.getEdition() != null) {
            Integer oldPlan = subscriber.getEdition().getPlan();
            Integer newPlan = edition.getPlan();

            if(oldPlan.equals(newPlan)) {
                Integer oldEdition = subscriber.getEdition().getId();
                Integer newEdition = edition.getId();

                if(
                    oldEdition.equals(newEdition) && 
                    !subscriber.getSubscription().getAutomaticBilling()
                ) {
                    // set automatic billing to true
                } else throw new SubscriptionAlreadyExists();
            } else {
                Map<Integer, Integer> counter = plansRepository.compareBenefitsById(oldPlan, newPlan);
                Integer oldBenefits = counter.getOrDefault(oldPlan, 0);
                Integer newBenefits = counter.getOrDefault(newPlan, 0);

                if(oldBenefits <= newBenefits) {
                    // upgrade
                } else {
                    // downgrade
                }; 
            };
        } else {
            this.subscriptionsRepository.subscribe(user.getUuid(), edition.getId());
        };
    };

    public void unsubscribe(User subscriber) {
        this.subscriptionsRepository.unsubscribe(subscriber.getUuid());
    };
};
