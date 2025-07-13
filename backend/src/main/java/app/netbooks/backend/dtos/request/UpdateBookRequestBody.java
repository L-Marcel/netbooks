package app.netbooks.backend.dtos.request;

import java.sql.Date;

import app.netbooks.backend.models.Author;
import app.netbooks.backend.models.Benefit;
import app.netbooks.backend.models.Publisher;
import app.netbooks.backend.models.Tag;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UpdateBookRequestBody {
    private Long isbn = null;
    private String title;
    private Date publishedIn;
    private Publisher publisher  = null;
    private String description;
    private Tag[] tags;
    private Author[] authors;
    private Benefit[] requirements;
};
