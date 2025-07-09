package app.netbooks.backend.errors;

import org.springframework.http.HttpStatus;

public class InvalidPdfFormat extends HttpError {
    public InvalidPdfFormat() {
        super("Formato de PDF inválido!", HttpStatus.UNPROCESSABLE_ENTITY);
    };
};