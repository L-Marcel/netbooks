package app.netbooks.backend.models;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class Subscription {
    private User subscriber;
    private PlanEdition edition;
    private LocalDate startedIn;
    private Integer payments;
    private Boolean closed;
};
