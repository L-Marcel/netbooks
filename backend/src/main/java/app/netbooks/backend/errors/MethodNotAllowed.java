package app.netbooks.backend.errors;

import org.springframework.http.HttpStatus;

public class MethodNotAllowed extends HttpError {
    public MethodNotAllowed() {
        super("Método não aceito!", HttpStatus.METHOD_NOT_ALLOWED);
    };
};