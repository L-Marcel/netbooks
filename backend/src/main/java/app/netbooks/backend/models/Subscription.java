package app.netbooks.backend.models;

import java.sql.Date;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class Subscription {
    private Long id;
    private UUID subscriber;
    private Integer edition;
    private Date startedIn;
    private Date closedIn;
    private Boolean automaticBilling;
    private Boolean actived;

    public Subscription(UUID subscriber, Integer edition) {
        this.subscriber = subscriber;
        this.edition = edition;
    };
};
