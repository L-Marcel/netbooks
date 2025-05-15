package app.netbooks.backend.errors;

import org.springframework.http.HttpStatus;

public class CannotConvertUuid extends HttpError {
    public CannotConvertUuid() {
        super("Não foi possível converter o uuid!", HttpStatus.INTERNAL_SERVER_ERROR);
    };
};