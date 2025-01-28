package bookingapp.service;

import static bookingapp.test.TestUtils.BOOKING_STATUS_CONFIRMED;
import static bookingapp.test.TestUtils.BOOKING_STUDIO_CONFIRMED;
import static bookingapp.test.TestUtils.BOOKING_STUDIO_PENDING;
import static bookingapp.test.TestUtils.DEFAULT_ID_ONE;
import static bookingapp.test.TestUtils.PAGEABLE;
import static bookingapp.test.TestUtils.PAID;
import static bookingapp.test.TestUtils.PAYMENT_PAGE;
import static bookingapp.test.TestUtils.PAYMENT_PAID;
import static bookingapp.test.TestUtils.PAYMENT_PAID_RESPONSE;
import static bookingapp.test.TestUtils.PAYMENT_PENDING;
import static bookingapp.test.TestUtils.PAYMENT_PENDING_RESPONSE;
import static bookingapp.test.TestUtils.PAYMENT_REQUEST_DTO;
import static bookingapp.test.TestUtils.PAYMENT_STATUS_PAID;
import static bookingapp.test.TestUtils.PAYMENT_STATUS_PENDING;
import static bookingapp.test.TestUtils.PENDING;
import static bookingapp.test.TestUtils.SESSION_ID;
import static bookingapp.test.TestUtils.SESSION_URL;
import static bookingapp.test.TestUtils.STATUS_CONFIRMED;
import static bookingapp.test.TestUtils.USER_CUSTOMER;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import bookingapp.dto.payment.PaymentResponse;
import bookingapp.mapper.PaymentMapper;
import bookingapp.model.payment.Payment;
import bookingapp.repository.booking.BookingRepository;
import bookingapp.repository.bookingstatus.BookingStatusRepository;
import bookingapp.repository.payment.PaymentRepository;
import bookingapp.repository.paymentstatus.PaymentStatusRepository;
import bookingapp.service.impl.PaymentServiceImpl;
import bookingapp.service.impl.StripeService;
import com.stripe.model.checkout.Session;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class PaymentServiceImplTest {
    @Mock
    private BookingRepository bookingRepository;
    @Mock
    private BookingStatusRepository bookingStatusRepository;
    @Mock
    private NotificationService notificationService;
    @Mock
    private PaymentMapper paymentMapper;
    @Mock
    private PaymentRepository paymentRepository;
    @Mock
    private PaymentStatusRepository paymentStatusRepository;
    @Mock
    private StripeService stripeService;
    @InjectMocks
    private PaymentServiceImpl paymentService;

    @Test
    @DisplayName("Verify getPayments() method with user id")
    void getPayments_WithUserId_ShouldReturnUsersPayments() {
        when(paymentRepository.findByBookingUserId(DEFAULT_ID_ONE, PAGEABLE))
                .thenReturn(List.of(PAYMENT_PENDING));
        when(paymentMapper.toDto(List.of(PAYMENT_PENDING)))
                .thenReturn(List.of(PAYMENT_PENDING_RESPONSE));

        List<PaymentResponse> actual = paymentService.getPayments(DEFAULT_ID_ONE, PAGEABLE);
        assertNotNull(actual);
        assertEquals(List.of(PAYMENT_PENDING_RESPONSE), actual);
        verify(paymentRepository).findByBookingUserId(DEFAULT_ID_ONE, PAGEABLE);
        verify(paymentMapper).toDto(List.of(PAYMENT_PENDING));
    }

    @Test
    @DisplayName("Verify getPayments() method without user id")
    void getPayments_WithoutUserId_ShouldReturnPaymentsList() {
        when(paymentRepository.findAll(PAGEABLE)).thenReturn(PAYMENT_PAGE);
        when(paymentMapper.toDto(PAYMENT_PAGE)).thenReturn(List.of(PAYMENT_PENDING_RESPONSE));

        List<PaymentResponse> actual = paymentService.getPayments(null, PAGEABLE);

        assertNotNull(actual);
        assertEquals(List.of(PAYMENT_PENDING_RESPONSE), actual);
        verify(paymentRepository).findAll(PAGEABLE);
        verify(paymentMapper).toDto(PAYMENT_PAGE);
    }

    @Test
    @DisplayName("Verify initiatePayment() method")
    void initiatePayment_ValidRequest_ReturnValidPaymentResponse() {
        Long bookingId = 1L;
        Long userId = 2L;
        Payment payment = PAYMENT_PENDING;
        Session session = mock(Session.class);
        PaymentResponse expected = PAYMENT_PENDING_RESPONSE;

        when(paymentMapper.toModel(PAYMENT_REQUEST_DTO)).thenReturn(payment);
        when(bookingRepository.findByIdAndUserId(bookingId, userId))
                .thenReturn(Optional.of(BOOKING_STUDIO_PENDING));
        when(paymentStatusRepository.findByStatus(PENDING))
                .thenReturn(Optional.of(PAYMENT_STATUS_PENDING));
        when(stripeService.createSession(payment)).thenReturn(session);
        when(session.getUrl()).thenReturn(SESSION_URL);
        when(session.getId()).thenReturn(SESSION_ID);
        when(paymentRepository.save(payment)).thenReturn(payment);
        when(paymentMapper.toDto(payment)).thenReturn(expected);

        PaymentResponse actual = paymentService
                .initiatePayment(USER_CUSTOMER, PAYMENT_REQUEST_DTO);
        assertNotNull(actual);
        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("Verify handleSuccessPayment() method")
    void handleSuccessPayment_ValidSessionId_ReturnValidPaymentResponse() {
        Session session = mock(Session.class);

        when(paymentRepository.findBySessionId(SESSION_ID))
                .thenReturn(Optional.of(PAYMENT_PENDING));
        when(stripeService.getSessionById(SESSION_ID)).thenReturn(session);
        when(session.getPaymentStatus()).thenReturn("paid");
        when(bookingStatusRepository.findByStatus(STATUS_CONFIRMED))
                .thenReturn(Optional.of(BOOKING_STATUS_CONFIRMED));
        when(paymentStatusRepository.findByStatus(PAID))
                .thenReturn(Optional.of(PAYMENT_STATUS_PAID));
        when(bookingRepository.save(BOOKING_STUDIO_PENDING))
                .thenReturn(BOOKING_STUDIO_CONFIRMED);
        when(paymentRepository.save(PAYMENT_PENDING)).thenReturn(PAYMENT_PAID);
        when(paymentMapper.toDto(PAYMENT_PENDING)).thenReturn(PAYMENT_PAID_RESPONSE);

        PaymentResponse actual = paymentService.handleSuccessPayment(SESSION_ID);
        assertNotNull(actual);
        assertEquals(PAYMENT_PAID_RESPONSE, actual);
    }

    @Test
    @DisplayName("Verify handleCancelPayment() method")
    void handleCancelPayment_ValidSessionId_ShouldInvokeSendNotificationReturnPaymentResponse() {
        Payment payment = PAYMENT_PENDING;
        PaymentResponse paymentResponse = PAYMENT_PENDING_RESPONSE;

        when(paymentRepository.findBySessionId(SESSION_ID)).thenReturn(Optional.of(payment));
        when(paymentMapper.toDto(payment)).thenReturn(paymentResponse);

        PaymentResponse actual = paymentService.handleCancelPayment(SESSION_ID);
        assertNotNull(actual);
        assertEquals(paymentResponse, actual);
    }
}
