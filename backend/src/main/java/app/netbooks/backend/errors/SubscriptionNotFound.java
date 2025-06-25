package app.netbooks.backend.errors;

import org.springframework.http.HttpStatus;

public class SubscriptionNotFound extends HttpError {
    public SubscriptionNotFound() {
        super("Assinatura não encontrado!", HttpStatus.NOT_FOUND);
    };
};