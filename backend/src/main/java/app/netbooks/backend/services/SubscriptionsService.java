package app.netbooks.backend.services;

import java.sql.Date;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import app.netbooks.backend.connections.transactions.Transactions;
import app.netbooks.backend.errors.PaymentNotFound;
import app.netbooks.backend.errors.PlanEditionNotFound;
import app.netbooks.backend.errors.PlanNotFound;
import app.netbooks.backend.errors.SubscriptionAlreadyExists;
import app.netbooks.backend.errors.SubscriptionNotFound;
import app.netbooks.backend.models.Payment;
import app.netbooks.backend.models.Plan;
import app.netbooks.backend.models.PlanEdition;
import app.netbooks.backend.models.Subscription;
import app.netbooks.backend.models.User;
import app.netbooks.backend.repositories.interfaces.PaymentsRepository;
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

    @Autowired
    private PaymentsRepository paymentsRepository;

    public Subscription findBySubscriber(UUID subscriber) throws SubscriptionNotFound {
        return this.subscriptionsRepository.findBySubscriber(subscriber)
            .orElseThrow(SubscriptionNotFound::new);
    };

    public Payment findLastPaymentBySubscriber(UUID subscriber) {
        return this.paymentsRepository.findLastPaymentBySubscriber(
            subscriber
        ).orElseThrow(PaymentNotFound::new);
    };

    public Optional<Subscription> searchBySubscriber(UUID subscriber) {
        return this.subscriptionsRepository.findBySubscriber(subscriber);
    };

    public void subscribe(
        User subscriber,
        PlanEdition newEdition
    ) throws SubscriptionAlreadyExists {
        transactions.run(() -> {
            Optional<Subscription> subscription = this.searchBySubscriber(subscriber.getUuid());

            Plan newPlan = plansRepository.findById(newEdition.getPlan())
                .orElseThrow(PlanNotFound::new);

            this.subscriptionsRepository.closeNotClosedBySubscriber(subscriber.getUuid());
            this.paymentsRepository.cancelScheduledPaymentsBySubscriber(subscriber.getUuid());

            if(subscription.isPresent()) {
                Integer oldEditionId = subscription.get().getEdition();
                Integer newEditionId = newEdition.getId();

                PlanEdition oldPlanEdition = this.plansEditionsRepository.findById(oldEditionId)
                    .orElseThrow(PlanEditionNotFound::new);
                
                Plan oldPlan = plansRepository.findById(oldPlanEdition.getPlan())
                    .orElseThrow(PlanNotFound::new);
                
                Integer oldPlanId = oldPlan.getId();
                Integer newPlanId = newPlan.getId();
                
                Payment lastPayment = this.findLastPaymentBySubscriber(subscriber.getUuid());

                if(oldPlanId.equals(newPlanId)) {
                    // same?
                } else {
                    Map<Integer, Integer> counter = plansRepository.compareBenefitsById(oldPlanId, newPlanId);
                    Integer oldBenefits = counter.getOrDefault(oldPlanId, 0);
                    Integer newBenefits = counter.getOrDefault(newPlanId, 0);
                    Boolean isUpgrade = oldBenefits <= newBenefits;

                    Subscription newSubscription = new Subscription(
                        subscriber.getUuid(), 
                        newEdition.getId()
                    );

                    if(isUpgrade) {
                        this.subscriptionsRepository.closeById(
                            subscription.get().getId()
                        );

                        this.subscriptionsRepository.subscribe(newSubscription);

                        Payment newPayment = new Payment(
                            newSubscription.getId(), 
                            newEdition.getPrice()
                        );

                        this.paymentsRepository.createFirst(newPayment);

                        // pay?
                    } else {
                        this.subscriptionsRepository.closeById(
                            subscription.get().getId(), 
                            lastPayment.getDueDate()
                        );

                        this.subscriptionsRepository.subscribe(newSubscription);

                        Payment newPayment = new Payment(
                            newSubscription.getId(), 
                            newEdition.getPrice(),
                            lastPayment.getDueDate()
                        );

                        this.paymentsRepository.create(newPayment);
                    }; 
                };
            } else {
                Subscription newSubscription = new Subscription(
                    subscriber.getUuid(), 
                    newEdition.getId()
                );
                
                this.subscriptionsRepository.subscribe(newSubscription);
                
                Payment newPayment = new Payment(
                    newSubscription.getId(), 
                    newEdition.getPrice()
                );
                
                this.paymentsRepository.createFirst(newPayment);

                // pay?
            };
        });

        // if(subscriber.getEdition() != null) {
        

        //     if(oldPlan.equals(newPlan)) {
        //         if(
        //             oldEdition.equals(newEdition) && 
        //             !subscriber.getSubscription().getAutomaticBilling()
        //         ) {
        //             // set automatic billing to true
        //         } else throw new SubscriptionAlreadyExists();
        //     } else {

        //     };
    };

    public void unsubscribe(User subscriber) {
        this.transactions.run(() -> {
            Subscription subscription = this.findBySubscriber(subscriber.getUuid());
            Payment lastPayment = this.findLastPaymentBySubscriber(subscriber.getUuid());

            this.paymentsRepository.cancelScheduledPaymentsBySubscriber(
                subscriber.getUuid()
            );

            this.subscriptionsRepository.closeById(
                subscription.getId(), 
                lastPayment.getDueDate()
            );
        });
    };
};
