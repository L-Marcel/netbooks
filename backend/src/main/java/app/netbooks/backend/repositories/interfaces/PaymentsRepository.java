package app.netbooks.backend.repositories.interfaces;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import app.netbooks.backend.models.Payment;

public interface PaymentsRepository {
    public Optional<Payment> findLastBySubscriber(UUID subscriber);
    public Optional<Payment> findLastPaidBySubscriber(UUID subscriber);
    public List<Payment> findAllBySubscriber(UUID subscriber);
    public void create(Payment payment);
    public void createFirst(Payment payment);
    public void cancelVisibleScheduledBySubscriber(UUID subscriber);
    public void deleteInvisibleScheduledBySubscriber(UUID subscriber);
    public void cancelById(Long id);
    public void deleteById(Long id);
    public void hiddenVisibleScheduledBySubscriber(UUID subscriber);
    public void showInvisibleScheduledBySubscriber(UUID subscriber);
    public void payById(Long id);
};
