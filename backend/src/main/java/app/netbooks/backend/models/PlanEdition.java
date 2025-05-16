package app.netbooks.backend.models;

import java.math.BigDecimal;
import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class PlanEdition {
    private Integer id;
    private BigDecimal price;
    private Plan plan;
    private LocalDate startedIn;
    private LocalDate closedIn;
};
