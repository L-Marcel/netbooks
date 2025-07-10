package app.netbooks.backend.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class Publisher {
    private String name;
    private Double score = 0d;

    public Publisher(String name) {
        this.name = name;
    };
};
