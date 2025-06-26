package app.netbooks.backend.repositories.interfaces;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import app.netbooks.backend.models.Payment;

public interface PaymentsRepository {
    public Optional<Payment> findLastPaymentBySubscriber(UUID subscriber);
    public Optional<Payment> findLastPaidPaymentBySubscriber(UUID subscriber);
    public List<Payment> findAllBySubscriber(UUID subscriber);
    public void create(Payment payment);
    public void createFirst(Payment payment);
    public void cancelVisibleScheduledPaymentsBySubscriber(UUID subscriber);
    public void deleteInvisibleScheduledPaymentsBySubscriber(UUID subscriber);
    public void hiddenVisibleScheduledPaymentsBySubscriber(UUID subscriber);
    public void payById(Long id);
};
