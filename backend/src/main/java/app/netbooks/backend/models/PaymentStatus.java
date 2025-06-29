package app.netbooks.backend.models;

public enum PaymentStatus {
    SCHEDULED,
    PAID,
    CANCELED,
    HIDDEN,
    UNKNOWN;

    public static PaymentStatus fromString(String status) {
        try {
            return PaymentStatus.valueOf(status);
        } catch (IllegalArgumentException e) {
            return PaymentStatus.UNKNOWN;
        }
    };
};
