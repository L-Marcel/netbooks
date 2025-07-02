package app.netbooks.backend.errors;

import org.springframework.http.HttpStatus;

public class BookFileNotFound extends HttpError {
    public BookFileNotFound() {
        super("Arquivo do livro não encontrado!", HttpStatus.NOT_FOUND);
    };
};