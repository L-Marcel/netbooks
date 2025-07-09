package app.netbooks.backend.errors;

import org.springframework.http.HttpStatus;

public class EmptyFile extends HttpError {
    public EmptyFile() {
        super("Arquivo vazio!", HttpStatus.UNPROCESSABLE_ENTITY);
    };
};