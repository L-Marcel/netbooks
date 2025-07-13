package app.netbooks.backend.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Publisher {
    private String name;
    private Double score = 0d;

    public Publisher(String name) {
        this.name = name;
    };
};
