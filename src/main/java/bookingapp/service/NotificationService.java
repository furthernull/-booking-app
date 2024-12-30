package bookingapp.service;

import bookingapp.model.accommodation.Accommodation;
import bookingapp.model.booking.Booking;
import bookingapp.model.payment.Payment;
import java.util.List;

public interface NotificationService {
    void sendNotification(Long userId, Booking booking);

    void sendNotification(Accommodation accommodation);

    void sendNotification(List<Booking> expiringBookings);

    void sendNotification(Payment payment);
}
