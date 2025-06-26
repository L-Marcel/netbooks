package app.netbooks.backend.services;

import java.math.BigDecimal;
import java.sql.Date;
import java.time.Duration;
import java.time.LocalDate;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import app.netbooks.backend.connections.Timezone;
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
    private Timezone timezone;

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

    private Payment findLastPaymentBySubscriber(UUID subscriber) {
        return this.paymentsRepository.findLastPaymentBySubscriber(
            subscriber
        ).orElseThrow(PaymentNotFound::new);
    };

    private Payment findLastPaidPaymentBySubscriber(UUID subscriber) {
        return this.paymentsRepository.findLastPaidPaymentBySubscriber(
            subscriber
        ).orElseThrow(PaymentNotFound::new);
    };

    public Optional<Subscription> searchBySubscriber(UUID subscriber) {
        return this.subscriptionsRepository.findBySubscriber(subscriber);
    };

    public void subscribe(
        UUID subscriber,
        PlanEdition newEdition
    ) throws SubscriptionAlreadyExists {
        transactions.run(() -> {
            Optional<Subscription> subscription = this.searchBySubscriber(subscriber);

            Plan newPlan = plansRepository.findById(newEdition.getPlan())
                .orElseThrow(PlanNotFound::new);

            this.subscriptionsRepository.closeNotClosedBySubscriber(subscriber);
            this.paymentsRepository.cancelScheduledPaymentsBySubscriber(subscriber);

            Integer newEditionId = newEdition.getId();

            if(subscription.isPresent()) {
                Integer oldEditionId = subscription.get().getEdition();

                PlanEdition oldPlanEdition = this.plansEditionsRepository.findById(oldEditionId)
                    .orElseThrow(PlanEditionNotFound::new);
                
                Plan oldPlan = plansRepository.findById(oldPlanEdition.getPlan())
                    .orElseThrow(PlanNotFound::new);
                
                Integer oldPlanId = oldPlan.getId();
                Integer newPlanId = newPlan.getId();
                
                Payment lastPayment = this.findLastPaymentBySubscriber(subscriber);
                Boolean isSamePlan = oldPlanId.equals(newPlanId);
                Boolean hasSubscriptionConflit = isSamePlan &&
                    oldEditionId.equals(newEditionId);
                
                if(hasSubscriptionConflit) throw new SubscriptionAlreadyExists();
                
                Boolean isUpgrade = !isSamePlan;
                if(isSamePlan) {
                    Map<Integer, Integer> counter = plansRepository.compareBenefitsById(oldPlanId, newPlanId);
                    Integer oldBenefits = counter.getOrDefault(oldPlanId, 0);
                    Integer newBenefits = counter.getOrDefault(newPlanId, 0);
                    isUpgrade = oldBenefits <= newBenefits;
                };

                Subscription newSubscription = new Subscription(
                    subscriber, 
                    newEditionId
                );

                if(isUpgrade) {
                    this.subscriptionsRepository.closeById(
                        subscription.get().getId()
                    );

                    this.subscriptionsRepository.subscribe(newSubscription);

                    Payment lastPaidPayment = this.findLastPaidPaymentBySubscriber(subscriber);
                    
                    Date oldEditionEndDate = Date.valueOf(lastPaidPayment.getDueDate().toLocalDate().plusDays(
                        newPlan.getDuration().toDays()
                    ));

                    Date currentDate = Date.valueOf(
                        LocalDate.now(
                            this.timezone.getZoneId()
                        )
                    );

                    Long rest = Duration.between(
                        oldEditionEndDate.toLocalDate(), 
                        currentDate.toLocalDate()
                    ).abs().toDays();

                    BigDecimal discount = lastPaidPayment.getPrice().multiply(
                        BigDecimal.valueOf(
                            Math.min(rest, 0) / 
                            Math.min(oldPlan.getDuration().toDays(), 0)
                        )
                    );

                    BigDecimal price = newEdition.getPrice().subtract(discount).min(
                        BigDecimal.ZERO
                    );
                    
                    Payment newPayment = new Payment(
                        newSubscription.getId(), 
                        price
                    );

                    this.paymentsRepository.createFirst(newPayment);
                    this.pay(newPayment);
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
            } else {
                Subscription newSubscription = new Subscription(
                    subscriber, 
                    newEditionId
                );
                
                this.subscriptionsRepository.subscribe(newSubscription);
                
                Payment newPayment = new Payment(
                    newSubscription.getId(), 
                    newEdition.getPrice()
                );
                
                this.paymentsRepository.createFirst(newPayment);
                this.pay(newPayment);
            };
        });
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

    private void pay(Payment payment) {
        this.transactions.run(() -> {
            this.paymentsRepository.payById(payment.getId());

            Subscription subscription = this.subscriptionsRepository.findById(
                payment.getSubscription()
            ).orElseThrow(SubscriptionNotFound::new);

            PlanEdition edition = this.plansEditionsRepository.findById(
                subscription.getEdition()
            ).orElseThrow(PlanEditionNotFound::new);

            Plan plan = this.plansRepository.findById(
                edition.getId()
            ).orElseThrow(PlanNotFound::new);

            if(edition.getAvailable()) {
                Payment newPayment = new Payment(
                    subscription.getId(), 
                    edition.getPrice(),
                    Date.valueOf(payment.getDueDate().toLocalDate().plusDays(
                        plan.getDuration().toDays()
                    ))
                );

                this.paymentsRepository.create(newPayment);
            } else if(plan.getAvailable()) {
                Optional<PlanEdition> newEdition = this.plansEditionsRepository.findBestByPlan(
                    plan.getId()
                );

                if(newEdition.isPresent()) {
                    this.subscribe(
                        subscription.getSubscriber(),
                        newEdition.get()
                    );
                };
            };
        });
    };

    public void payLastPaymentBySubscriber(UUID subscriber) {
        this.transactions.run(() -> {
            Payment lastPayment = this.findLastPaymentBySubscriber(subscriber);
            this.pay(lastPayment);
        });
    };
};
