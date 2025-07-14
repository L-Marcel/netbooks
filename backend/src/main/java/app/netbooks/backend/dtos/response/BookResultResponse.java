package app.netbooks.backend.dtos.response;

import java.util.ArrayList;
import java.util.List;

import app.netbooks.backend.models.Book;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BookResultResponse {
    private Long id;

    public BookResultResponse(Book book){
        this.id = book.getId();
    }

    public static List<BookResultResponse> toBookResultResponseList(List<Book> books) {
        List<BookResultResponse> responseList = new ArrayList<>();
        for (Book book : books) {
            responseList.add(new BookResultResponse(book));
        }
        return responseList;
    }
}
