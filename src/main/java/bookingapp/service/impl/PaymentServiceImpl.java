package bookingapp.service.impl;

import bookingapp.dto.payment.PaymentRequestDto;
import bookingapp.dto.payment.PaymentResponse;
import bookingapp.exception.EntityNotFoundException;
import bookingapp.exception.UrlCreationException;
import bookingapp.mapper.PaymentMapper;
import bookingapp.model.booking.Booking;
import bookingapp.model.payment.Payment;
import bookingapp.model.payment.PaymentStatus;
import bookingapp.model.user.User;
import bookingapp.repository.booking.BookingRepository;
import bookingapp.repository.payment.PaymentRepository;
import bookingapp.repository.paymentstatus.PaymentStatusRepository;
import bookingapp.service.PaymentService;
import com.stripe.model.checkout.Session;
import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.temporal.ChronoUnit;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class PaymentServiceImpl implements PaymentService {
    private static final PaymentStatus.Status PENDING_STATUS = PaymentStatus.Status.PENDING;
    private final BookingRepository bookingRepository;
    private final PaymentMapper paymentMapper;
    private final PaymentRepository paymentRepository;
    private final PaymentStatusRepository paymentStatusRepository;
    private final StripeService stripeService;

    @Override
    public List<PaymentResponse> getPayments(Long userId, Pageable pageable) {
        if (userId != null) {
            return paymentMapper.toDto(paymentRepository.findByBookingUserId(userId, pageable));
        }
        return paymentMapper.toDto(paymentRepository.findAll(pageable));
    }

    @Transactional
    @Override
    public PaymentResponse initiatePayment(User user, PaymentRequestDto requestDto) {
        Payment payment = paymentMapper.toModel(requestDto);
        PaymentStatus paymentStatus = paymentStatusRepository.findByStatus(PENDING_STATUS)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Can't retrieve status PENDING from DB"));
        Booking booking = bookingRepository
                .findByIdAndUserId(payment.getBooking().getId(), user.getId())
                .orElseThrow(
                        () -> new EntityNotFoundException(
                                "Can't fetch booking by id: " + requestDto.bookingId()));
        payment.setStatus(paymentStatus);
        payment.setBooking(booking);
        payment.setAmountToPay(calculateAmount(booking));
        Session session = stripeService.createSession(payment);
        payment.setSessionUrl(getUrl(session.getUrl()));
        payment.setSessionId(session.getId());
        paymentRepository.save(payment);
        return paymentMapper.toDto(payment);
    }

    private URL getUrl(String url) {
        try {
            return new URL(url);
        } catch (MalformedURLException ex) {
            throw new UrlCreationException("URL creation failure. " + ex.getMessage());
        }
    }

    private BigDecimal calculateAmount(Booking booking) {
        long days = ChronoUnit.DAYS.between(booking.getCheckInDate(), booking.getCheckOutDate());
        return BigDecimal.valueOf(days).multiply(booking.getAccommodation().getDailyRate());
    }
}
