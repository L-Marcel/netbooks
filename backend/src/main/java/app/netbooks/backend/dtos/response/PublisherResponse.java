package app.netbooks.backend.dtos.response;

import java.util.List;
import java.util.stream.Collectors;

import app.netbooks.backend.models.Publisher;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PublisherResponse {
    private String name;
    private Double score;

    public PublisherResponse(Publisher publisher) {
        this.name = publisher.getName();
        this.score = publisher.getScore();
    };

    public static List<PublisherResponse> fromList(List<Publisher> list) {
        return list.stream()
            .map(PublisherResponse::new)
            .collect(Collectors.toList());
    };
};

