package app.netbooks.backend.dtos.response;

import java.util.List;
import java.util.stream.Collectors;

import app.netbooks.backend.models.Benefit;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BenefitResponse {
    private String name;

    public BenefitResponse(Benefit benefit) {
        this.name = benefit.getName();
    };

    public static List<BenefitResponse> fromList(List<Benefit> list) {
        return list.stream()
            .map(BenefitResponse::new)
            .collect(Collectors.toList());
    };
};

