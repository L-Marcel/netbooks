package app.netbooks.backend.errors;

import org.springframework.http.HttpStatus;

public class BookNotFound extends HttpError {
    public BookNotFound() {
        super("Livro não encontrado!", HttpStatus.NOT_FOUND);
    };
};