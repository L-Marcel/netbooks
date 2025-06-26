package app.netbooks.backend.services;

import java.math.BigDecimal;
import java.sql.Date;
import java.time.Duration;
import java.time.LocalTime;
import java.util.List;
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
import app.netbooks.backend.repositories.interfaces.PaymentsRepository;
import app.netbooks.backend.repositories.interfaces.PlansEditionsRepository;
import app.netbooks.backend.repositories.interfaces.PlansRepository;
import app.netbooks.backend.repositories.interfaces.SubscriptionsRepository;
import app.netbooks.backend.utils.Server;

@Service
public class SubscriptionsService {
    @Autowired
    private Transactions transactions;
    
    @Autowired
    private Server server;

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

    public Subscription findNextScheduledBySubscriber(UUID subscriber) throws SubscriptionNotFound {
        return this.subscriptionsRepository.findNextScheduledBySubscriber(subscriber)
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

    private Optional<Subscription> searchBySubscriber(UUID subscriber) {
        return this.subscriptionsRepository.findBySubscriber(subscriber);
    };

    public void closedScheduledsBySubscriber(UUID subscriber, Boolean automaticBilling) {
        this.transactions.run(() -> {
            this.subscriptionsRepository.closedScheduledsBySubscriber(subscriber);
            this.subscriptionsRepository.closeNotClosedBySubscriber(subscriber);
            this.paymentsRepository.deleteInvisibleScheduledPaymentsBySubscriber(subscriber);
            this.paymentsRepository.cancelVisibleScheduledPaymentsBySubscriber(subscriber);
            this.paymentsRepository.hiddenVisibleScheduledPaymentsBySubscriber(subscriber);
            Optional<Subscription> subscription = this.searchBySubscriber(subscriber);
            if(subscription.isPresent()) {
                PlanEdition oldPlanEdition = this.plansEditionsRepository.findById(
                    subscription.get().getEdition()
                ).orElseThrow(PlanEditionNotFound::new);

                Payment lastPayment = this.findLastPaymentBySubscriber(subscriber);

                Payment newPayment = new Payment(
                    subscription.get().getId(), 
                    oldPlanEdition.getPrice(),
                    lastPayment.getDueDate()
                );

                this.paymentsRepository.create(newPayment);
            };
        });
    };

    public void subscribe(
        UUID subscriber,
        Boolean automaticBilling,
        PlanEdition newEdition
    ) throws SubscriptionAlreadyExists {
        transactions.run(() -> {
            Date currentDate = this.server.getServerCurrentDate();
            Optional<Subscription> subscription = this.searchBySubscriber(subscriber);

            Plan newPlan = plansRepository.findById(newEdition.getPlan())
                .orElseThrow(PlanNotFound::new);
            
            this.subscriptionsRepository.closedScheduledsBySubscriber(subscriber);
            this.subscriptionsRepository.closeNotClosedBySubscriber(subscriber);
            this.paymentsRepository.deleteInvisibleScheduledPaymentsBySubscriber(subscriber);
            this.paymentsRepository.cancelVisibleScheduledPaymentsBySubscriber(subscriber);
            this.paymentsRepository.hiddenVisibleScheduledPaymentsBySubscriber(subscriber);

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
                
                Boolean isUpgrade = false;
                if(!isSamePlan) {
                    Map<Integer, Integer> counter = plansRepository.compareBenefitsById(oldPlanId, newPlanId);
                    Integer oldBenefits = counter.getOrDefault(oldPlanId, 0);
                    Integer newBenefits = counter.getOrDefault(newPlanId, 0);
                    isUpgrade = oldBenefits <= newBenefits;
                };

                if(isUpgrade) {
                    Subscription newSubscription = new Subscription(
                        subscriber, 
                        newEditionId,
                        currentDate
                    );

                    this.subscriptionsRepository.closeById(
                        subscription.get().getId()
                    );

                    this.subscriptionsRepository.subscribe(
                        newSubscription
                    );

                    Payment lastPaidPayment = this.findLastPaidPaymentBySubscriber(subscriber);
                    
                    Date oldEditionEndDate = Date.valueOf(lastPaidPayment.getDueDate().toLocalDate().plusDays(
                        oldPlan.getDuration().toDays()
                    ));

                    Long rest = Duration.between(
                        oldEditionEndDate.toLocalDate().atTime(LocalTime.MIN), 
                        currentDate.toLocalDate().atTime(LocalTime.MIN)
                    ).abs().toDays();

                    BigDecimal discount = lastPaidPayment.getPrice().multiply(
                        BigDecimal.valueOf(
                            Math.min(rest, 0) / 
                            Math.min(oldPlan.getDuration().toDays(), 1)
                        )
                    );

                    BigDecimal price = newEdition.getPrice().subtract(discount).max(
                        BigDecimal.ZERO
                    );
                    
                    Payment newPayment = new Payment(
                        newSubscription.getId(), 
                        price
                    );

                    this.paymentsRepository.createFirst(newPayment);
                    this.pay(subscriber, newPayment, automaticBilling);
                } else {
                    Subscription newSubscription = new Subscription(
                        subscriber, 
                        newEditionId,
                        lastPayment.getDueDate()
                    );

                    this.subscriptionsRepository.closeById(
                        subscription.get().getId(), 
                        lastPayment.getDueDate()
                    );

                    this.subscriptionsRepository.subscribe(
                        newSubscription
                    );

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
                    newEditionId,
                    currentDate
                );
                
                this.subscriptionsRepository.subscribe(
                    newSubscription
                );
                
                Payment newPayment = new Payment(
                    newSubscription.getId(), 
                    newEdition.getPrice()
                );
                
                this.paymentsRepository.createFirst(newPayment);
                this.pay(subscriber, newPayment, automaticBilling);
            };
        });
    };

    public void renew(UUID subscriber, Boolean automaticBilling) {
        this.transactions.run(() -> {
            Payment lastPayment = this.findLastPaymentBySubscriber(subscriber);

            Subscription subscription = this.subscriptionsRepository.findById(
                lastPayment.getSubscription()
            ).orElseThrow(SubscriptionNotFound::new);

            PlanEdition edition = this.plansEditionsRepository.findById(
                subscription.getEdition()
            ).orElseThrow(PlanEditionNotFound::new);

            Plan plan = this.plansRepository.findById(
                edition.getPlan()
            ).orElseThrow(PlanNotFound::new);

            if(edition.getAvailable() && automaticBilling) {
                Payment newPayment = new Payment(
                    subscription.getId(), 
                    edition.getPrice(),
                    Date.valueOf(lastPayment.getDueDate().toLocalDate().plusDays(
                        plan.getDuration().toDays()
                    ))
                );

                this.paymentsRepository.create(newPayment);
            } else if(plan.getAvailable() && automaticBilling) {
                Optional<PlanEdition> newEdition = this.plansEditionsRepository.findBestByPlan(
                    plan.getId()
                );

                if(newEdition.isPresent()) {
                    this.subscribe(
                        subscription.getSubscriber(),
                        automaticBilling,
                        newEdition.get()
                    );
                };
            };
        });
    };

    private void pay(UUID subscriber, Payment payment, Boolean automaticBilling) {
        // [TODO] Falta o ignorado paid_at
        this.transactions.run(() -> {
            this.paymentsRepository.payById(payment.getId());
            this.renew(subscriber, automaticBilling);
        });
    };

    public void payLastPaymentBySubscriber(UUID subscriber, Boolean automaticBilling) {
        this.transactions.run(() -> {
            Payment lastPayment = this.findLastPaymentBySubscriber(subscriber);
            this.pay(subscriber, lastPayment, automaticBilling);
        });
    };
    
    public List<Payment> findAllPaymentsBySubscriber(UUID subscriber) {
        return this.paymentsRepository.findAllBySubscriber(subscriber);
    };
};
