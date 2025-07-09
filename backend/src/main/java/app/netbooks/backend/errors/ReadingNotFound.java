package app.netbooks.backend.errors;

import org.springframework.http.HttpStatus;

public class ReadingNotFound extends HttpError {
    public ReadingNotFound() {
        super("Leitura não encontrado!", HttpStatus.NOT_FOUND);
    };
};