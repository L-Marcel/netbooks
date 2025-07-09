package app.netbooks.backend.models;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class Classification {
    private Long book;
    private UUID user;
    private Integer value;
};
