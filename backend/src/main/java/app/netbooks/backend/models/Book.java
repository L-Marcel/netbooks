package app.netbooks.backend.models;

import java.sql.Date;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class Book {
    private Long id;
    private Long isbn;
    private String title;
    private String description;
    private Double stars;
    private Integer numPages;
    private Date publishedIn;
    private Publisher publisher;
};
