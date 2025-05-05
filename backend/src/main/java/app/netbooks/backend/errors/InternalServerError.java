package app.netbooks.backend.errors;

import org.springframework.http.HttpStatus;

public class InternalServerError extends HttpError {
    public InternalServerError() {
        super("Erro interno no servidor!", HttpStatus.INTERNAL_SERVER_ERROR);
    };
};
