package app.netbooks.backend.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Author {
    private Long id;
    private String name;
    private Double score = 0d;

    public Author(Long id, String name) {
        this.id = id;
        this.name = name;
    };

    public Author(String name){
        this.name = name;
    };
};
