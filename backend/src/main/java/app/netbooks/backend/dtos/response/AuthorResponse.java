package app.netbooks.backend.dtos.response;

import java.util.List;
import java.util.stream.Collectors;

import app.netbooks.backend.models.Author;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AuthorResponse {
    private Long id;
    private String name;
    private Double score;

    public AuthorResponse(Author author) {
        this.id = author.getId();
        this.name = author.getName();
        this.score = author.getScore();
    };

    public static List<AuthorResponse> fromList(List<Author> list) {
        return list.stream()
            .map(AuthorResponse::new)
            .collect(Collectors.toList());
    };
};

