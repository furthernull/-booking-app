package bookingapp.service.impl;

import bookingapp.exception.StripeServiceException;
import bookingapp.model.payment.Payment;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import java.math.BigDecimal;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

@Service
public class StripeService {
    private static final Long DEFAULT_QUANTITY = 1L;
    private static final Long CENTS = 100L;
    private static final String DEFAULT_CURRENCY = "usd";
    private static final String SUCCESS_PATH = "/api/payments/success/";
    private static final String CANCEL_PATH = "/api/payments/cancel/";
    @Value("${base.url}")
    private String baseUrl;

    Session createSession(Payment payment) {
        SessionCreateParams params = SessionCreateParams.builder()
                .addPaymentMethodType(SessionCreateParams.PaymentMethodType.CARD)
                .setMode(SessionCreateParams.Mode.PAYMENT)
                .setSuccessUrl(buildUrl(SUCCESS_PATH))
                .setCancelUrl(buildUrl(CANCEL_PATH))
                .addLineItem(getLineItem(payment))
                .build();
        try {
            return Session.create(params);
        } catch (StripeException ex) {
            throw new StripeServiceException("Session build failure. " + ex.getMessage());
        }
    }

    public Session getSessionById(String sessionId) {
        try {
            return Session.retrieve(sessionId);
        } catch (StripeException e) {
            throw new StripeServiceException("Fetch session failure." + e.getMessage());
        }
    }

    private SessionCreateParams.LineItem getLineItem(Payment payment) {
        return SessionCreateParams.LineItem.builder()
                .setQuantity(DEFAULT_QUANTITY)
                .setPriceData(getPriceData(payment))
                .build();
    }

    private SessionCreateParams.LineItem.PriceData getPriceData(Payment payment) {
        return SessionCreateParams.LineItem.PriceData.builder()
                .setCurrency(DEFAULT_CURRENCY)
                .setUnitAmountDecimal(getUnitAmount(payment.getAmountToPay()))
                .setProductData(getProductInfo(payment))
                .build();
    }

    private SessionCreateParams.LineItem.PriceData.ProductData getProductInfo(Payment payment) {
        return SessionCreateParams.LineItem.PriceData.ProductData.builder()
                .setName("Booking #" + payment.getBooking().getId())
                .build();
    }

    private String buildUrl(String path) {
        return UriComponentsBuilder.fromHttpUrl(baseUrl)
                .path(path)
                .queryParam("sessionId", "{CHECKOUT_SESSION_ID}")
                .build()
                .toUriString();
    }

    private BigDecimal getUnitAmount(BigDecimal amountToPay) {
        return amountToPay.multiply(BigDecimal.valueOf(CENTS));
    }
}
