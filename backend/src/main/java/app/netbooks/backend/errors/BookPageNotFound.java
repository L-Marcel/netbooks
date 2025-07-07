package app.netbooks.backend.errors;

import org.springframework.http.HttpStatus;

public class BookPageNotFound extends HttpError {
    public BookPageNotFound() {
        super("Página do livro não encontrado!", HttpStatus.NOT_FOUND);
    };
};