package bookingapp.service;

import bookingapp.dto.payment.PaymentResponse;
import java.util.List;
import org.springframework.data.domain.Pageable;

public interface PaymentService {
    List<PaymentResponse> getPayments(Long userId, Pageable pageable);
}
