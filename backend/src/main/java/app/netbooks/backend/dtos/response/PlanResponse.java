package app.netbooks.backend.dtos.response;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import app.netbooks.backend.models.Benefit;
import app.netbooks.backend.models.Plan;
import app.netbooks.backend.models.PlanEdition;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PlanResponse {
    private Integer id;
    private String name;
    private String description;
    private Integer numSubscribers;
    private Long duration;
    private List<BenefitResponse> benefits;
    private List<PlanEditionResponse> editions;

    public PlanResponse(Plan plan, List<Benefit> benefits, List<PlanEdition> editions) {
        this.id = plan.getId();
        this.name = plan.getName();
        this.description = plan.getDescription();
        this.numSubscribers = plan.getNumSubscribers();
        this.duration = plan.getDuration().toDays();
        this.benefits = BenefitResponse.fromList(benefits);
        this.editions = PlanEditionResponse.fromList(editions);
    };

    public static List<PlanResponse> fromList(
        List<Plan> list, 
        Map<Integer, List<Benefit>> mappedBenefits, 
        Map<Integer, List<PlanEdition>> mappedEditions
    ) {
        return list.stream()
            .map((plan) -> {
                List<Benefit> benefits = mappedBenefits.getOrDefault(plan.getId(), new LinkedList<>());
                List<PlanEdition> editions = mappedEditions.getOrDefault(plan.getId(), new LinkedList<>());
                return new PlanResponse(plan, benefits, editions);
            }).collect(Collectors.toList());
    };
};
