package app.netbooks.backend.dtos.response;

import java.sql.Date;
import java.util.UUID;

import app.netbooks.backend.models.PlanEdition;
import app.netbooks.backend.models.Subscription;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SubscriptionResponse {
    private Long id;
    private UUID subscriber;
    private PlanEditionResponse edition;
    private Date startedIn;
    private Date closedIn;
    private Boolean automaticBilling;
    private Boolean actived;

    public SubscriptionResponse(Subscription subscription, PlanEdition edition) {
        this.id = subscription.getId();
        this.subscriber = subscription.getSubscriber();
        this.edition = new PlanEditionResponse(edition);
        this.startedIn = subscription.getStartedIn();
        this.closedIn = subscription.getClosedIn();
        this.automaticBilling = subscription.getAutomaticBilling();
        this.actived = subscription.getActived();
    };
};
