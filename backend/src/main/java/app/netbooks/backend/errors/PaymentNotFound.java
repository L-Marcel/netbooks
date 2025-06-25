package app.netbooks.backend.errors;

import org.springframework.http.HttpStatus;

public class PaymentNotFound extends HttpError {
    public PaymentNotFound() {
        super("Pagamento não encontrado!", HttpStatus.NOT_FOUND);
    };
};