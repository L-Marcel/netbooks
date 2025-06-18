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
};

