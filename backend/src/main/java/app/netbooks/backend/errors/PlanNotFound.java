package app.netbooks.backend.errors;

import org.springframework.http.HttpStatus;

public class PlanNotFound extends HttpError {
    public PlanNotFound() {
        super("Plano não encontrado!", HttpStatus.NOT_FOUND);
    };
};