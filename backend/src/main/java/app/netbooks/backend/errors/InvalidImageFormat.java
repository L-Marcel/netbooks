package app.netbooks.backend.errors;

import org.springframework.http.HttpStatus;

public class InvalidImageFormat extends HttpError {
    public InvalidImageFormat() {
        super("Formato de imagem inválido!", HttpStatus.UNPROCESSABLE_ENTITY);
    };
};