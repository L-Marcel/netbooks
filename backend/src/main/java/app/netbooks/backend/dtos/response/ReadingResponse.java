package app.netbooks.backend.dtos.response;

import java.sql.Date;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import app.netbooks.backend.models.Reading;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ReadingResponse {
    private Long id;
    private Date startedIn;
    private Date stoppedIn;
    private Boolean finished;
    private Integer currentPage;
    private Integer numPages;
    private Double percentage;
    private UUID user;
    private Long book;

    public ReadingResponse(
        Reading reading
    ) {
        this.id = reading.getId();
        this.startedIn = reading.getStartedIn();
        this.stoppedIn = reading.getStoppedIn();
        this.finished = reading.getFinished();
        this.currentPage = reading.getCurrentPage();
        this.numPages = reading.getNumPages();
        this.percentage = reading.getPercentage();
        this.user = reading.getUser();
        this.book = reading.getBook();
    };

    public static List<ReadingResponse> fromList(
        List<Reading> list
    ) {
        return list.stream()
            .map((reading) -> new ReadingResponse(reading))
            .collect(Collectors.toList());
    };
};

