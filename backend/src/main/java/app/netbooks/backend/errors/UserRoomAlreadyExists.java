package app.netbooks.backend.errors;

import org.springframework.http.HttpStatus;

public class UserRoomAlreadyExists extends HttpError {
    public UserRoomAlreadyExists() {
        super(
            "Usuário já possui uma sala em execução!", 
            HttpStatus.CONFLICT
        );
    };
};
