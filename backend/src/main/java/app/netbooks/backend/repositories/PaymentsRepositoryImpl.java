package app.netbooks.backend.repositories;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Repository;

import app.netbooks.backend.connections.interfaces.Database;
import app.netbooks.backend.models.Payment;
import app.netbooks.backend.models.PaymentStatus;
import app.netbooks.backend.repositories.interfaces.PaymentsRepository;

@Repository
public class PaymentsRepositoryImpl extends BaseRepository implements PaymentsRepository {
    public PaymentsRepositoryImpl(Database database) {
        super(database);
    };

    @Override
    public Long create(Payment payment) {
        return this.query((connection) -> {
            Long id = null;

            try (
                PreparedStatement statement = connection.prepareStatement(
                    // language=sql
                    """
                    INSERT INTO payment (subscription, price, pay_date, due_date, status) 
                    VALUES (?, ?, ?, ?, ?);
                    """
                );
            ) {
                statement.setLong(1, payment.getSubscription());
                statement.setBigDecimal(2, payment.getPrice());
                statement.setDate(3, payment.getPayDate());
                statement.setDate(4, payment.getDueDate());
                statement.setString(5, payment.getStatus().toString());
                statement.executeUpdate();

                try (ResultSet result = statement.getGeneratedKeys()) {
                    result.next();
                    id = result.getLong(1);
                };
            };

            return id;
        });
    };

    @Override
    public Long createFirst(Payment payment) {
        return this.query((connection) -> {
            Long id = null;

            try (
                PreparedStatement statement = connection.prepareStatement(
                    // language=sql
                    """
                    INSERT INTO payment (subscription, price, pay_date, due_date, status) 
                    VALUES (?, ?, CURRENT_DATE, CURRENT_DATE, ?);
                    """,
                    true
                );
            ) {
                statement.setLong(1, payment.getSubscription());
                statement.setBigDecimal(2, payment.getPrice());
                statement.setString(3, payment.getStatus().toString());
                statement.executeUpdate();

                try (ResultSet result = statement.getGeneratedKeys()) {
                    result.next();
                    id = result.getLong(1);
                };
            };

            return id;
        });
    };

    @Override
    public void cancelScheduledPaymentsBySubscriber(UUID subscriber) {
        this.execute((connection) -> {
           try (
                PreparedStatement statement = connection.prepareStatement(
                    // language=sql
                    """
                    UPDATE payment AS pay
                    JOIN subscription AS sub
                    SET pay.status = 'CANCELED'
                    WHERE pay.status = 'SCHEDULED'
                    AND pay.subscription = sub.id
                    AND sub.subscriber = ?;
                    """
                );
            ) {
                statement.setString(1, subscriber.toString());
                statement.executeUpdate();
            }; 
        });
    };

    @Override
    public Optional<Payment> findLastPaymentBySubscriber(UUID subscriber) {
        return this.queryOrDefault((connection) -> {
            Optional<Payment> paymentFound = Optional.empty();

            try (
                PreparedStatement statement = connection.prepareStatement(
                    // language=sql
                    """
                    SELECT DISTINCT pay.* FROM payment AS pay
                    JOIN subscription AS sub
                    ON pay.subscription = sub.id
                    WHERE sub.subscriber = ?
                    ORDER BY pay.created_at DESC, pay.id DESC
                    LIMIT 1;
                    """
                );
            ) {
                statement.setString(1, subscriber.toString());
                try (ResultSet result = statement.executeQuery()) {
                    if(result.next()) {
                        Long id = result.getLong("id");
                        Long subscription = result.getLong("subscription");
                        BigDecimal price = result.getBigDecimal("price");
                        Date payDate = result.getDate("pay_date");
                        Date dueDate = result.getDate("due_date");
                        Timestamp createdAt = result.getTimestamp("created_at");
                        PaymentStatus status = PaymentStatus.fromString(result.getString("status"));

                        Payment payment = new Payment(
                            id,
                            subscription,
                            price,
                            payDate,
                            dueDate,
                            createdAt,
                            status
                        );

                        paymentFound = Optional.of(payment);
                    };
                };
            };

            return paymentFound;
        }, Optional.empty());
    }

    @Override
    public void payById(Long id) {
        // [TODO]
    };
};
