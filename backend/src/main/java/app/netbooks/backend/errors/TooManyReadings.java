package app.netbooks.backend.errors;

import org.springframework.http.HttpStatus;

public class TooManyReadings extends HttpError {
    public TooManyReadings() {
        super("Muitas leituras em andamento!", HttpStatus.BAD_REQUEST);
    };
};

