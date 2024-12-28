package bookingapp.exception;

public class StripeServiceException extends RuntimeException {
    public StripeServiceException(String string) {
        super(string);
    }
}
