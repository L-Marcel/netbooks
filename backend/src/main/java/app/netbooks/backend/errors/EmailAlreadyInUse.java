package app.netbooks.backend.errors;

import org.springframework.http.HttpStatus;

public class EmailAlreadyInUse extends HttpError {
    public EmailAlreadyInUse() {
        super("E-mail já se encontra em uso!", HttpStatus.CONFLICT);
    };
};