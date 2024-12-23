package bookingapp.service.impl;

import bookingapp.dto.payment.PaymentResponse;
import bookingapp.mapper.PaymentMapper;
import bookingapp.repository.payment.PaymentRepository;
import bookingapp.service.PaymentService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class PaymentServiceImpl implements PaymentService {
    private final PaymentMapper paymentMapper;
    private final PaymentRepository paymentRepository;

    @Override
    public List<PaymentResponse> getPayments(Long userId, Pageable pageable) {
        if (userId != null) {
            return paymentMapper.toDto(paymentRepository.findByBookingUserId(userId, pageable));
        }
        return paymentMapper.toDto(paymentRepository.findAll(pageable));
    }
}
