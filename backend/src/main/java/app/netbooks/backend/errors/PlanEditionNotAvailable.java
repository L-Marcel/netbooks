package app.netbooks.backend.errors;

import org.springframework.http.HttpStatus;

public class PlanEditionNotAvailable extends HttpError {
    public PlanEditionNotAvailable() {
        super("Edição do plano indisponível!", HttpStatus.NOT_FOUND);
    };
};