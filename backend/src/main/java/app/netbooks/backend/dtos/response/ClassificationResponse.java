package app.netbooks.backend.dtos.response;

import app.netbooks.backend.models.Classification;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ClassificationResponse {
    private Integer value;

    public ClassificationResponse(
       Classification classification
    ) {
        this.value = classification.getValue();
    };
};
