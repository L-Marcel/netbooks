package app.netbooks.backend.errors;

import org.springframework.http.HttpStatus;

public class UserNotFound extends HttpError {
    public UserNotFound() {
        super("Usuário não encontrado!", HttpStatus.NOT_FOUND);
    };
};