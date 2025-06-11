package app.netbooks.backend.dtos.response;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.List;
import java.util.stream.Collectors;

import app.netbooks.backend.models.PlanEdition;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PlanEditionResponse {
    private Integer id;
    private Integer plan;
    private Integer popularity;
    private BigDecimal price;
    private Date startedIn;
    private Date closedIn;

    public PlanEditionResponse(PlanEdition edition) {
        this.id = edition.getId();
        this.plan = edition.getPlan();
        this.popularity = edition.getPopularity();
        this.price = edition.getPrice();
        this.startedIn = edition.getStartedIn();
        this.closedIn = edition.getClosedIn();
    };

    public static List<PlanEditionResponse> fromList(List<PlanEdition> list) {
        return list.stream()
            .map(PlanEditionResponse::new)
            .collect(Collectors.toList());
    };
};
