package app.netbooks.backend.errors;

import org.springframework.http.HttpStatus;

public class AuthorNotFound extends HttpError {
    public AuthorNotFound() {
        super("Autor não encontrado!", HttpStatus.NOT_FOUND);
    }
}