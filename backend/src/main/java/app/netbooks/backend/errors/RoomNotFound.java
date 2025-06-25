package app.netbooks.backend.errors;

import org.springframework.http.HttpStatus;

public class RoomNotFound extends HttpError {
    public RoomNotFound() {
        super("Sala não encontrada!", HttpStatus.NOT_FOUND);
    };
};