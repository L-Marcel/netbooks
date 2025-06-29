package app.netbooks.backend.dtos.response;

import java.sql.Date;

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
public class RenewDetailsResponse {
    private Boolean hidden;
    private Date payDate;
    private Date dueDate;

    public RenewDetailsResponse(Payment payment) {
        this.payDate = payment.getPayDate();
        this.dueDate = payment.getDueDate();
        this.hidden = payment.getStatus().equals(PaymentStatus.HIDDEN);
    };
};
