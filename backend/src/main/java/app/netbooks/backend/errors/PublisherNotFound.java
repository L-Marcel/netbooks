package app.netbooks.backend.errors;

import org.springframework.http.HttpStatus;

public class PublisherNotFound extends HttpError {
    public PublisherNotFound() {
        super("Editora não encontrada!", HttpStatus.NOT_FOUND);
    };
};