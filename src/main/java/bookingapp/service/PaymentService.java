package bookingapp.service;

import bookingapp.dto.payment.PaymentRequestDto;
import bookingapp.dto.payment.PaymentResponse;
import bookingapp.model.user.User;
import java.util.List;
import org.springframework.data.domain.Pageable;

public interface PaymentService {
    List<PaymentResponse> getPayments(Long userId, Pageable pageable);

    PaymentResponse initiatePayment(User user, PaymentRequestDto requestDto);
}
