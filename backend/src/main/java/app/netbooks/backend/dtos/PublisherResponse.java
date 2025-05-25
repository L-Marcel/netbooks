package app.netbooks.backend.dtos;

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

    public PublisherResponse(Publisher publisher) {
        this.name = publisher.getName();
    };

    public static List<PublisherResponse> fromList(List<Publisher> list) {
        return list.stream()
            .map(PublisherResponse::new)
            .collect(Collectors.toList());
    };
};

