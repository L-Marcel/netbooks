package app.netbooks.backend.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import app.netbooks.backend.annotations.AuhenticatedOnly;
import app.netbooks.backend.annotations.SubscriberOnly;
import app.netbooks.backend.authentication.AuthenticatedUser;
import app.netbooks.backend.dtos.response.PaymentResponse;
import app.netbooks.backend.dtos.response.SubscriptionResponse;
import app.netbooks.backend.models.Payment;
import app.netbooks.backend.models.PlanEdition;
import app.netbooks.backend.models.Subscription;
import app.netbooks.backend.services.PlansEditionsService;
import app.netbooks.backend.services.SubscriptionsService;

@RestController
@RequestMapping("/subscriptions")
public class SubscriptionsController {
    @Autowired
    private PlansEditionsService plansEditionService;

    @Autowired
    private SubscriptionsService subscriptionsService;

    @SubscriberOnly
    @GetMapping("/me")
    public ResponseEntity<SubscriptionResponse> findBySubscriber(
        @AuthenticationPrincipal AuthenticatedUser user
    ) {
        Subscription subscription = subscriptionsService.findBySubscriber(
            user.getUser().getUuid()
        );

        PlanEdition edition = plansEditionService.findById(
            subscription.getEdition()
        );

        SubscriptionResponse response = new SubscriptionResponse(
            subscription, 
            edition
        );
        return ResponseEntity.ok().body(response);
    };

    @AuhenticatedOnly
    @GetMapping("/me/next")
    public ResponseEntity<SubscriptionResponse> findNextScheduledBySubscriber(
        @AuthenticationPrincipal AuthenticatedUser user
    ) {
        Subscription subscription = subscriptionsService.findNextScheduledBySubscriber(
            user.getUser().getUuid()
        );

        PlanEdition edition = plansEditionService.findById(
            subscription.getEdition()
        );

        SubscriptionResponse response = new SubscriptionResponse(
            subscription, 
            edition
        );
        return ResponseEntity.ok().body(response);
    };

    @AuhenticatedOnly
    @PostMapping("/me/next/cancel")
    public ResponseEntity<SubscriptionResponse> closedScheduledsBySubscriber(
        @AuthenticationPrincipal AuthenticatedUser user
    ) {
        subscriptionsService.closedScheduledsBySubscriber(
            user.getUser().getUuid(),
            user.getUser().getAutomaticBilling()
        );

        return ResponseEntity.ok().build();
    };

    @AuhenticatedOnly
    @GetMapping("/me/payments")
    public ResponseEntity<List<PaymentResponse>> findAllPaymentsBySubscriber(
        @AuthenticationPrincipal AuthenticatedUser user
    ) {
        List<Payment> payments = subscriptionsService.findAllPaymentsBySubscriber(
            user.getUser().getUuid()
        );

        List<PaymentResponse> response = PaymentResponse.fromList(payments);
        return ResponseEntity.ok().body(response);
    };

    @AuhenticatedOnly
    @PostMapping("/subscribe/{id}")
    public ResponseEntity<Void> subscribe(
        @AuthenticationPrincipal AuthenticatedUser user,
        @PathVariable Integer id
    ) {
        PlanEdition edition = plansEditionService.findAvailableById(id);

        subscriptionsService.subscribe(
            user.getUser().getUuid(),
            user.getUser().getAutomaticBilling(),
            edition
        );

        return ResponseEntity.status(HttpStatus.CREATED).build();
    };
};
