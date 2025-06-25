package app.netbooks.backend.models;

import java.math.BigDecimal;
import java.sql.Date;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class PlanEdition {
    private Integer id;
    private Integer plan;
    private Integer numSubscribers;
    private BigDecimal price;
    private Date startedIn;
    private Date closedIn;
    private Boolean available;
};
