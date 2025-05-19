package app.netbooks.backend.errors;

public class ValidationStepError extends RuntimeException {
    public ValidationStepError(String message) {
        super(message);
    };
};
