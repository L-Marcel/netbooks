package app.netbooks.backend.repositories.interfaces;

import java.util.Optional;
import java.util.UUID;

import app.netbooks.backend.models.Payment;

public interface PaymentsRepository {
    public Optional<Payment> findLastPaymentBySubscriber(UUID subscriber);
    public void create(Payment payment);
    public void createFirst(Payment payment);
    public void cancelScheduledPaymentsBySubscriber(UUID subscriber);
};
