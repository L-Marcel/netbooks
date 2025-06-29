package app.netbooks.backend.dtos.response;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.List;
import java.util.stream.Collectors;

import app.netbooks.backend.models.Payment;
import app.netbooks.backend.models.PaymentStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PaymentResponse {
    private Long id;
    private Long subscription;
    private BigDecimal price;
    private Date payDate;
    private Date dueDate;
    private Timestamp createdAt;
    private Timestamp paidAt;
    private PaymentStatus status;

    private ProductResponse product;

    public PaymentResponse(Payment payment) {
        this.id = payment.getId();
        this.subscription = payment.getSubscription();
        this.price = payment.getPrice();
        this.payDate = payment.getPayDate();
        this.dueDate = payment.getDueDate();
        this.createdAt = payment.getCreatedAt();
        this.paidAt = payment.getPaidAt();
        this.status = payment.getStatus();
        this.product = new ProductResponse(payment.getProduct());
    };

    public static List<PaymentResponse> fromList(List<Payment> list) {
        return list.stream()
            .map(PaymentResponse::new)
            .collect(Collectors.toList());
    };
};
