package app.netbooks.backend.errors;

import org.springframework.http.HttpStatus;

public class ReadingAlreadyFinished extends HttpError {
    public ReadingAlreadyFinished() {
        super("Leitura não encontrado!", HttpStatus.LOCKED);
    };
};