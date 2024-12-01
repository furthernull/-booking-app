package bookingapp.service;

import bookingapp.model.accommodation.Accommodation;
import bookingapp.model.booking.Booking;

public interface NotificationService {
    void sendNotification(Long userId, Booking booking);

    void sendNotification(Accommodation accommodation);
}
