package app.netbooks.backend.dtos.response;

import java.sql.Date;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import app.netbooks.backend.models.Author;
import app.netbooks.backend.models.Benefit;
import app.netbooks.backend.models.Book;
import app.netbooks.backend.models.Tag;

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
    private List<BenefitResponse> requirements;

    public BookResponse(
        Book book, 
        List<Author> authors, 
        List<Tag> tags, 
        List<Benefit> requirements
    ) {
        this.id = book.getId();
        this.isbn = book.getIsbn();
        this.title = book.getTitle();
        this.description = book.getDescription();
        this.stars = book.getStars();
        this.numPages = book.getNumPages();
        this.publishedIn = book.getPublishedIn();
        this.publisher = new PublisherResponse(book.getPublisher());
        this.authors = AuthorResponse.fromList(authors);
        this.tags = TagResponse.fromList(tags);
        this.requirements = BenefitResponse.fromList(requirements);
    };

    public static List<BookResponse> fromList(
        List<Book> list,
        Map<Long, List<Author>> mappedAuthors,
        Map<Long, List<Tag>> mappedTags,
        Map<Long, List<Benefit>> mappedRequirements
    ) {
        return list.stream()
            .map((book) -> {
                List<Author> authors = mappedAuthors.getOrDefault(book.getId(), new LinkedList<>());
                List<Tag> tags = mappedTags.getOrDefault(book.getId(), new LinkedList<>());
                List<Benefit> requirements = mappedRequirements.getOrDefault(book.getId(), new LinkedList<>());
                return new BookResponse(book, authors, tags, requirements);
            }).collect(Collectors.toList());
    };
};

