package app.netbooks.backend.dtos.response;

import java.sql.Date;
import java.util.List;
import java.util.stream.Collectors;

import app.netbooks.backend.models.Book;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BookResponse {
    private Long id;
    private Long isbn;
    private String title;
    private String description;
    private Double stars;
    private Integer numPages;
    private Date publishedIn;
    private PublisherResponse publisher;
    private List<AuthorResponse> authors;
    private List<TagResponse> tags;

    public BookResponse(Book book) {
        this.id = book.getId();
        this.isbn = book.getIsbn();
        this.title = book.getTitle();
        this.description = book.getDescription();
        this.stars = book.getStars();
        this.numPages = book.getNumPages();
        this.publishedIn = book.getPublishedIn();
        this.publisher = new PublisherResponse(book.getPublisher());
        this.authors = AuthorResponse.fromList(book.getAuthors());
        this.tags = TagResponse.fromList(book.getTags());
    };

    public static List<BookResponse> fromList(List<Book> list) {
        return list.stream()
            .map(BookResponse::new)
            .collect(Collectors.toList());
    };
};

