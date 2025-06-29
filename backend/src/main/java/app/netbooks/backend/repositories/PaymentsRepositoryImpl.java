package app.netbooks.backend.repositories;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.time.Duration;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Repository;

import app.netbooks.backend.connections.interfaces.Database;
import app.netbooks.backend.models.Payment;
import app.netbooks.backend.models.PaymentStatus;
import app.netbooks.backend.models.Product;
import app.netbooks.backend.repositories.interfaces.PaymentsRepository;

@Repository
public class PaymentsRepositoryImpl extends BaseRepository implements PaymentsRepository {
    public PaymentsRepositoryImpl(Database database) {
        super(database);
    };

    @Override
    public void create(Payment payment) {
        this.execute((connection) -> {
            try (
                PreparedStatement statement = connection.prepareStatement(
                    // language=sql
                    """
                    INSERT INTO payment (subscription, price, pay_date, due_date, paid_at, status) 
                    VALUES (?, ?, ?, ?, ?, ?);
                    """,
                    true
                );
            ) {
                statement.setLong(1, payment.getSubscription());
                statement.setBigDecimal(2, payment.getPrice());
                statement.setDate(3, payment.getPayDate());
                statement.setDate(4, payment.getDueDate());
                statement.setTimestamp(5, payment.getPaidAt());
                statement.setString(6, payment.getStatus().toString());
                statement.executeUpdate();

                try (ResultSet result = statement.getGeneratedKeys()) {
                    result.next();
                    Long id = result.getLong(1);
                    payment.setId(id);
                };
            };
        });
    };

    @Override
    public void createFirst(Payment payment) {
        this.execute((connection) -> {
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
                    Long id = result.getLong(1);
                    payment.setId(id);
                };
            };
        });
    };

    @Override
    public void deleteInvisibleScheduledBySubscriber(UUID subscriber) {
        this.execute((connection) -> {
           try (
                PreparedStatement statement = connection.prepareStatement(
                    // language=sql
                    """
                    DELETE pay FROM payment AS pay
                    JOIN subscription AS sub
                    ON pay.subscription = sub.id
                    WHERE pay.status = 'SCHEDULED'
                    AND pay.pay_date > CURRENT_DATE
                    AND sub.started_in > CURRENT_DATE
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
    public void cancelVisibleScheduledBySubscriber(UUID subscriber) {
        this.execute((connection) -> {
           try (
                PreparedStatement statement = connection.prepareStatement(
                    // language=sql
                    """
                    UPDATE payment AS pay
                    JOIN subscription AS sub
                    ON pay.subscription = sub.id
                    SET pay.status = 'CANCELED'
                    WHERE pay.status = 'SCHEDULED'
                    AND sub.started_in <= CURRENT_DATE
                    AND pay.pay_date <= CURRENT_DATE
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
    public void deleteById(Long id) {
        this.execute((connection) -> {
           try (
                PreparedStatement statement = connection.prepareStatement(
                    // language=sql
                    """
                    DELETE FROM payment AS pay
                    WHERE pay.id = ?;
                    """
                );
            ) {
                statement.setLong(1, id);
                statement.executeUpdate();
            }; 
        });
    };

    @Override
    public void cancelById(Long id) {
        this.execute((connection) -> {
           try (
                PreparedStatement statement = connection.prepareStatement(
                    // language=sql
                    """
                    UPDATE payment AS pay
                    SET pay.status = 'CANCELED'
                    WHERE pay.id = ?;
                    """
                );
            ) {
                statement.setLong(1, id);
                statement.executeUpdate();
            };
        });
    };

    @Override
    public void hiddenVisibleScheduledBySubscriber(UUID subscriber) {
        this.execute((connection) -> {
           try (
                PreparedStatement statement = connection.prepareStatement(
                    // language=sql
                    """
                    UPDATE payment AS pay
                    JOIN subscription AS sub
                    ON pay.subscription = sub.id
                    LEFT JOIN (
                        SELECT DISTINCT sub.subscriber 
                        FROM payment AS pay
                        JOIN subscription AS sub
                        ON pay.subscription = sub.id
                        AND pay.status = 'HIDDEN'
                        AND pay.pay_date > CURRENT_DATE
                    ) AS sch
                    ON sch.subscriber = sub.subscriber
                    SET pay.status = 'HIDDEN'
                    WHERE pay.status = 'SCHEDULED'
                    AND sub.started_in <= CURRENT_DATE
                    AND pay.pay_date > CURRENT_DATE
                    AND sch.subscriber IS NULL
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
    public void showInvisibleScheduledBySubscriber(UUID subscriber) {
        this.execute((connection) -> {
           try (
                PreparedStatement statement = connection.prepareStatement(
                    // language=sql
                    """
                    UPDATE payment AS pay
                    JOIN subscription AS sub
                    ON pay.subscription = sub.id
                    LEFT JOIN (
                        SELECT DISTINCT sub.subscriber 
                        FROM payment AS pay
                        JOIN subscription AS sub
                        ON pay.subscription = sub.id
                        AND pay.status = 'SCHEDULED'
                        AND pay.pay_date > CURRENT_DATE
                    ) AS sch
                    ON sch.subscriber = sub.subscriber
                    SET pay.status = 'SCHEDULED'
                    WHERE pay.status = 'HIDDEN'
                    AND sub.started_in <= CURRENT_DATE
                    AND pay.pay_date > CURRENT_DATE
                    AND sch.subscriber IS NULL
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
    public Optional<Payment> findLastBySubscriber(UUID subscriber) {
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
                    AND pay.due_date >= CURRENT_DATE
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
                        Timestamp paidAt = result.getTimestamp("paid_at");
                        PaymentStatus status = PaymentStatus.fromString(result.getString("status"));

                        Payment payment = new Payment(
                            id,
                            subscription,
                            price,
                            payDate,
                            dueDate,
                            createdAt,
                            paidAt,
                            status
                        );

                        paymentFound = Optional.of(payment);
                    };
                };
            };

            return paymentFound;
        }, Optional.empty());
    };

    @Override
    public Optional<Payment> findLastPaidBySubscriber(UUID subscriber) {
        return this.queryOrDefault((connection) -> {
            Optional<Payment> paymentFound = Optional.empty();

            try (
                PreparedStatement statement = connection.prepareStatement(
                    // language=sql
                    """
                    SELECT DISTINCT pay.* FROM payment AS pay
                    JOIN subscription AS sub
                    ON pay.subscription = sub.id
                    WHERE sub.subscriber = ? AND pay.status = 'PAID'
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
                        Timestamp paidAt = result.getTimestamp("paid_at");
                        PaymentStatus status = PaymentStatus.fromString(result.getString("status"));

                        Payment payment = new Payment(
                            id,
                            subscription,
                            price,
                            payDate,
                            dueDate,
                            createdAt,
                            paidAt,
                            status
                        );

                        paymentFound = Optional.of(payment);
                    };
                };
            };

            return paymentFound;
        }, Optional.empty());
    };

    @Override
    public void payById(Long id) {
        this.execute((connection) -> {
            try (
                PreparedStatement statement = connection.prepareStatement(
                    // language=sql
                    """
                    UPDATE payment
                    SET status = 'PAID',
                    paid_at = CURRENT_TIMESTAMP
                    WHERE id = ?
                    """
                );
            ) {
                statement.setLong(1, id);
                statement.executeUpdate();
            };
        });
    }

    @Override
    public List<Payment> findAllBySubscriber(UUID subscriber) {
        return this.queryOrDefault((connection) -> {
            List<Payment> payments = new LinkedList<>();

            try (
                PreparedStatement preparedStatement = connection.prepareStatement(
                    // language=sql
                    """
                    SELECT * FROM subscriber_payments 
                    WHERE subscriber = ?;
                    """
                );
            ) {
                preparedStatement.setString(1, subscriber.toString());
                
                try (ResultSet result = preparedStatement.executeQuery()) {
                    while(result.next()) {
                        Long id = result.getLong("id");
                        Long subscription = result.getLong("subscription");
                        BigDecimal price = result.getBigDecimal("price");
                        Date payDate = result.getDate("pay_date");
                        Date dueDate = result.getDate("due_date");
                        Timestamp createdAt = result.getTimestamp("created_at");
                        Timestamp paidAt = result.getTimestamp("paid_at");
                        PaymentStatus status = PaymentStatus.fromString(result.getString("status"));

                        String productName = result.getString("name");
                        Duration productDuration = Duration.ofDays(result.getLong("duration"));
                        
                        Product product = new Product(
                            productName,
                            productDuration
                        );

                        Payment payment = new Payment(
                            id,
                            subscription,
                            price,
                            payDate,
                            dueDate,
                            createdAt,
                            paidAt,
                            status,
                            product
                        );

                        payments.add(payment);
                    };
                };
            };

            return payments;
        }, new LinkedList<>());
    };
};
