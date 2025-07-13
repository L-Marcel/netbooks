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
    private Double score;

    public Book(
        Long isbn,
        String title,
        String description,
        Integer numPages,
        Date publishedIn,
        Publisher publisher
    ) {
        this.isbn = isbn;
        this.title = title;
        this.description = description;
        this.numPages = numPages;
        this.publishedIn = publishedIn;
        this.publisher = publisher;
    };

    public Book(
        Long id,
        Long isbn,
        String title,
        String description,
        Double stars,
        Integer numPages,
        Date publishedIn,
        Publisher publisher
    ) {
        this.id = id;
        this.isbn = isbn;
        this.title = title;
        this.description = description;
        this.stars = stars;
        this.numPages = numPages;
        this.publishedIn = publishedIn;
        this.publisher = publisher;
    };
};
