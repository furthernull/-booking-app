package bookingapp.service.impl;

import bookingapp.exception.StripeServiceException;
import bookingapp.model.payment.Payment;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import jakarta.annotation.PostConstruct;
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
    @Value("${stripe.secret.key}")
    private String secretKey;
    @Value("${base.url}")
    private String baseUrl;

    @PostConstruct
    public void init() {
        Stripe.apiKey = secretKey;
    }

    Session createSession(Payment payment) {
        SessionCreateParams params = SessionCreateParams.builder()
                .setSuccessUrl(getSuccessUrl())
                .setCancelUrl(getCancelUrl())
                .addLineItem(getLineItem(payment))
                .setMode(SessionCreateParams.Mode.PAYMENT)
                .addPaymentMethodType(SessionCreateParams.PaymentMethodType.CARD)
                .build();
        try {
            return Session.create(params);
        } catch (StripeException ex) {
            throw new StripeServiceException("Session build failure. " + ex.getMessage());
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

    private BigDecimal getUnitAmount(BigDecimal amountToPay) {
        return amountToPay.multiply(BigDecimal.valueOf(CENTS));
    }

    private String getSuccessUrl() {
        return UriComponentsBuilder.fromHttpUrl(baseUrl)
                .path(SUCCESS_PATH)
                .toUriString();
    }

    private String getCancelUrl() {
        return UriComponentsBuilder.fromHttpUrl(baseUrl)
                .path(CANCEL_PATH)
                .toUriString();
    }
}
