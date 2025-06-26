package app.netbooks.backend.dtos.response;

import app.netbooks.backend.models.Product;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProductResponse {
    private String name;
    private Long duration;

    public ProductResponse(Product product) {
        this.name = product.getName();
        this.duration = product.getDuration().toDays();
    };
};
