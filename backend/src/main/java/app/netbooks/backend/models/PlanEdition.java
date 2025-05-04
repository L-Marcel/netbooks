package app.netbooks.backend.models;

import java.time.LocalDate;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class PlanEdition {
    private UUID uuid;
    private double price;
    private Plan plan;
    private LocalDate startedAt;
    private LocalDate closedAt;
};
