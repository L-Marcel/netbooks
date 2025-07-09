package app.netbooks.backend.errors;

import org.springframework.http.HttpStatus;

public class ClassificationNotFound extends HttpError {
    public ClassificationNotFound() {
        super("Classificação não encontrada!", HttpStatus.NOT_FOUND);
    };
};