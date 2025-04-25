package app.netbooks.backend.models;

import java.time.Duration;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class Plan {
    private UUID uuid;
    private String name;
    private String description;
    private Duration duration;

    public Plan(String name, String description, Duration duration) {
        this.name = name;
        this.description = description;
        this.duration = duration;
    };
};