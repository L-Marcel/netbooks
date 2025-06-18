package app.netbooks.backend.services;

import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import app.netbooks.backend.connections.transactions.Transactions;
import app.netbooks.backend.errors.SubscriptionAlreadyExists;
import app.netbooks.backend.errors.SubscriptionNotFound;
import app.netbooks.backend.models.PlanEdition;
import app.netbooks.backend.models.Subscription;
import app.netbooks.backend.models.User;
import app.netbooks.backend.repositories.interfaces.PlansEditionsRepository;
import app.netbooks.backend.repositories.interfaces.PlansRepository;
import app.netbooks.backend.repositories.interfaces.SubscriptionsRepository;

@Service
public class SubscriptionsService {
    @Autowired
    private Transactions transactions;

    @Autowired
    private SubscriptionsRepository subscriptionsRepository;

    @Autowired
    private PlansRepository plansRepository;

    @Autowired
    private PlansEditionsRepository plansEditionsRepository;

    public Subscription findBySubscriber(UUID subscriber) throws SubscriptionNotFound {
        return this.subscriptionsRepository.findBySubscriber(subscriber)
            .orElseThrow(SubscriptionNotFound::new);
    };

    public Optional<Subscription> searchBySubscriber(UUID subscriber) {
        return this.subscriptionsRepository.findBySubscriber(subscriber);
    };

    public void subscribe(
        User subscriber, 
        PlanEdition edition
    ) throws SubscriptionAlreadyExists {
        transactions.run(() -> {
            subscriptionsRepository.createSubscriberIfNotExists(subscriber.getUuid());
            Optional<Subscription> subscription = subscriptionsRepository.findBySubscriber(subscriber.getUuid());
            subscriptionsRepository.closeOldSubscriptions(subscriber.getUuid());

            if(subscription.isPresent()) {
                //upgrade, downgrade or same?
                subscriptionsRepository.closeOldSubscriptions(subscriber.getUuid());
            } else {
                subscriptionsRepository.closeOldSubscriptions(subscriber.getUuid());
                Long newSubscriptionId = subscriptionsRepository.subscribe(subscriber.getUuid(), edition.getId());
                
                // try (
                //     PreparedStatement paymentStatement = connection.prepareStatement(
                //         "INSERT INTO payment (subscription, price, pay_date, due_date, status) VALUES\n" +
                //         " (?, ?, ?, ?, 'SCHEDULED');"
                //     );
                // ) {
                //     paymentStatement.setLong(1, subscription);
                //     paymentStatement.setString(2, subscriber.toString());
                //     paymentStatement.executeUpdate();
                // };
            };
        });

        // if(subscriber.getEdition() != null) {
        //     Integer oldPlan = subscriber.getEdition().getPlan();
        //     Integer newPlan = edition.getPlan();
        //     Integer oldEdition = subscriber.getEdition().getId();
        //     Integer newEdition = edition.getId();

        //     if(oldPlan.equals(newPlan)) {
        //         if(
        //             oldEdition.equals(newEdition) && 
        //             !subscriber.getSubscription().getAutomaticBilling()
        //         ) {
        //             // set automatic billing to true
        //         } else throw new SubscriptionAlreadyExists();
        //     } else {
        //         Map<Integer, Integer> counter = plansRepository.compareBenefitsById(oldPlan, newPlan);
        //         Integer oldBenefits = counter.getOrDefault(oldPlan, 0);
        //         Integer newBenefits = counter.getOrDefault(newPlan, 0);

        //         if(oldBenefits <= newBenefits) {
        //             this.subscriptionsRepository.upgrade(
        //                 user.getUuid(), 
        //                 subscriber.getSubscription().getId(),
        //                 newEdition
        //             );
        //         } else {
        //             this.subscriptionsRepository.downgrade(
        //                 user.getUuid(), 
        //                 subscriber.getSubscription().getId(),
        //                 newEdition
        //             );
        //         }; 
        //     };
    };

    public void unsubscribe(User subscriber) {
        this.subscriptionsRepository.unsubscribe(subscriber.getUuid());
    };
};
