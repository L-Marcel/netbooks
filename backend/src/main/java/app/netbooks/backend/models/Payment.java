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
    private PaymentStatus status;

    public Payment(
        Long subscription,
        BigDecimal price,
        Date dueDate
    ) {
        this.subscription = subscription;
        this.price = price;
        this.payDate = Date.valueOf(dueDate.toLocalDate().minusDays(7l));
        this.dueDate = dueDate;
        this.status = PaymentStatus.SCHEDULED;
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
