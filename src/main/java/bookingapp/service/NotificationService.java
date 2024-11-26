package bookingapp.service;

public interface NotificationService {
    void sendNotification(Long userId, Long bookingId);

    void sendNotification(Long accommodationId);
}
