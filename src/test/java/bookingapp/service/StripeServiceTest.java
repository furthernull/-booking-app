package bookingapp.service;

import static bookingapp.test.TestUtils.INVALID_SESSION_ID;
import static bookingapp.test.TestUtils.PAYMENT_PENDING;
import static bookingapp.test.TestUtils.SESSION_ID;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;

import bookingapp.exception.StripeServiceException;
import bookingapp.service.impl.StripeService;
import com.stripe.exception.ApiException;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

@ExtendWith(MockitoExtension.class)
class StripeServiceTest {
    @InjectMocks
    private StripeService stripeService;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(stripeService, "baseUrl", "http://localhost:8088");
    }

    @Test
    @DisplayName("Verify createSession() method")
    void createSession_ValidPayment_ReturnSession() throws StripeException {
        try (MockedStatic<Session> sessionMockedStatic = Mockito.mockStatic(Session.class)) {
            sessionMockedStatic.when(() -> Session.create(any(SessionCreateParams.class)))
                    .thenReturn(new Session());

            Session actual = stripeService.createSession(PAYMENT_PENDING);

            assertNotNull(actual);
        }
    }

    @Test
    @DisplayName("Verify getSessionById() with valid session ID")
    void getSessionById_ValidSessionId_ReturnsSession() throws StripeException {
        Session mockSession = new Session();

        try (MockedStatic<Session> sessionMockedStatic = Mockito.mockStatic(Session.class)) {
            sessionMockedStatic.when(() -> Session.retrieve(SESSION_ID)).thenReturn(mockSession);

            Session session = stripeService.getSessionById(SESSION_ID);

            assertNotNull(session);
            assertEquals(mockSession, session);
        }
    }

    @Test
    @DisplayName("Verify getSessionById() throws exception for invalid session ID")
    void getSessionById_InvalidSessionId_ThrowsStripeServiceException() {
        try (MockedStatic<Session> sessionMockedStatic = Mockito.mockStatic(Session.class)) {
            sessionMockedStatic.when(() -> Session.retrieve(INVALID_SESSION_ID))
                    .thenThrow(new ApiException("Session not found", null, null, null, null));

            StripeServiceException exception = assertThrows(StripeServiceException.class, () -> {
                stripeService.getSessionById(INVALID_SESSION_ID);
            });

            assertEquals("Fetch session failure.Session not found", exception.getMessage());
        }
    }
}
