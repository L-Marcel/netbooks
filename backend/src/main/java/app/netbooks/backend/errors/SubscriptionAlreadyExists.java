package app.netbooks.backend.errors;

import org.springframework.http.HttpStatus;

public class SubscriptionAlreadyExists extends HttpError {
    public SubscriptionAlreadyExists() {
        super("Assinatura já existe!", HttpStatus.CONFLICT);
    };
};