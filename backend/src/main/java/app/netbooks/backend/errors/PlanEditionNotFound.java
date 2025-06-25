package app.netbooks.backend.errors;

import org.springframework.http.HttpStatus;

public class PlanEditionNotFound extends HttpError {
    public PlanEditionNotFound() {
        super("Edição do plano não encontrada!", HttpStatus.NOT_FOUND);
    };
};