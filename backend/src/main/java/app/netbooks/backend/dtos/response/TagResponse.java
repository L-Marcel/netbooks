package app.netbooks.backend.dtos.response;

import java.util.List;
import java.util.stream.Collectors;

import app.netbooks.backend.models.Tag;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TagResponse {
    private String name;
    private Double score;

    public TagResponse(Tag tag) {
        this.name = tag.getName();
        this.score = tag.getScore();
    };

    public static List<TagResponse> fromList(List<Tag> list) {
        return list.stream()
            .map(TagResponse::new)
            .collect(Collectors.toList());
    };
};

