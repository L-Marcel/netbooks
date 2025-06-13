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
    private Date dueDate;
    private Date payDate;
    private Integer numPayments;
    private Boolean automaticBilling;
    private Boolean actived;
};
