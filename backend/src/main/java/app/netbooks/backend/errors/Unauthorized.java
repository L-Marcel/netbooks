package app.netbooks.backend.errors;

import org.springframework.http.HttpStatus;

public class Unauthorized extends HttpError {
    public Unauthorized() {
        super("Acesso negado!", HttpStatus.UNAUTHORIZED);
    };
};
