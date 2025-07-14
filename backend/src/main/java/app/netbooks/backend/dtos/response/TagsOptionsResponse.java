package app.netbooks.backend.dtos.response;

import java.util.ArrayList;
import java.util.List;

import app.netbooks.backend.models.Tag;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TagsOptionsResponse {
    private String name;

    public static List<TagsOptionsResponse> toTagsOptionsResponseList(List<Tag> tags) {
        List<TagsOptionsResponse> responseList = new ArrayList<>();
        for (Tag tag : tags) {
            responseList.add(new TagsOptionsResponse(tag.getName()));
        }
        return responseList;
    }
}
