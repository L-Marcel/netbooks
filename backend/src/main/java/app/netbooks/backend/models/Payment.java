package app.netbooks.backend.models;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class Payment {
    private Long id;
    private Long subscription;
    private BigDecimal price;
    private Date payDate;
    private Date dueDate;
    private Timestamp createdAt;
    private Timestamp paidAt;
    private PaymentStatus status;

    private Product product;

    public Payment(
        Long id,
        Long subscription,
        BigDecimal price,
        Date payDate,
        Date dueDate,
        Timestamp createdAt,
        Timestamp paidAt,
        PaymentStatus status
    ) {
        this(subscription, price, dueDate);
        this.id = id;
        this.payDate = payDate;
        this.createdAt = createdAt;
        this.status = status;
    };

    public Payment(
        Long subscription,
        BigDecimal price,
        Date dueDate
    ) {
        this(subscription, price);
        this.payDate = Date.valueOf(dueDate.toLocalDate().minusDays(7l));
        this.dueDate = dueDate;
    };

    public Payment(
        Long subscription,
        BigDecimal price
    ) {
        this.subscription = subscription;
        this.price = price;
        this.status = PaymentStatus.SCHEDULED;
    };
};
