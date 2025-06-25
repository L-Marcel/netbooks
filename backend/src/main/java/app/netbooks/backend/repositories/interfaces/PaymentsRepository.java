package app.netbooks.backend.repositories.interfaces;

import java.util.Optional;
import java.util.UUID;

import app.netbooks.backend.models.Payment;

public interface PaymentsRepository {
    public Optional<Payment> findLastPaymentBySubscriber(UUID subscriber);
    public Long create(Payment payment);
    public Long createFirst(Payment payment);
    public void cancelScheduledPaymentsBySubscriber(UUID subscriber);
    public void payById(Long id);
};
