package app.netbooks.backend.models;

import java.sql.Date;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class Reading {
    private Long id;
    private Date startedIn;
    private Date stoppedIn;
    private Boolean finished;
    private Integer currentPage;
    private Integer numPages;
    private Double percentage;
    private UUID user;
    private Long book;

    public Reading (
        UUID user,
        Long book
    ) {
        this.user = user;
        this.book = book;
    };
};
