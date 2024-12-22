package bookingapp.dto.payment;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PaymentResponse {
    private Long id;
    private String paymentStatus;
    private String bookingId;
    private String sessionUrl;
    private String sessionId;
    private String amount;
}
