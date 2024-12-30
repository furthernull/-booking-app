package bookingapp.service.impl;

import bookingapp.exception.EntityNotFoundException;
import bookingapp.model.accommodation.Accommodation;
import bookingapp.model.accommodation.Address;
import bookingapp.model.accommodation.AmenityType;
import bookingapp.model.booking.Booking;
import bookingapp.model.payment.Payment;
import bookingapp.model.telegram.TelegramChat;
import bookingapp.repository.telegram.TelegramRepository;
import bookingapp.service.NotificationService;
import bookingapp.telegram.NotificationTemplates;
import bookingapp.telegram.TelegramBot;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class TelegramNotificationService implements NotificationService {
    public static final String AMENITIES_DELIMITER = ", ";
    private final TelegramBot telegramBot;
    private final TelegramRepository telegramRepository;

    @Override
    public void sendNotification(Long userId, Booking booking) {
        TelegramChat userChat = getChat(userId);
        if (userChat != null && userChat.isSubscribed()) {
            String notification = prepareNotification(userChat, booking);
            telegramBot.sendMessage(userChat.getChatId(), notification);
        }
    }

    @Override
    public void sendNotification(Accommodation accommodation) {
        String notification = prepareNotification(accommodation);
        telegramRepository.findAllByIsSubscribedIsTrue()
                .forEach(c -> telegramBot.sendMessage(c.getChatId(), notification));
    }

    @Override
    public void sendNotification(List<Booking> expiringBookings) {
        if (expiringBookings.isEmpty()) {
            List<TelegramChat> admins = telegramRepository.fetchAdminChats();
            admins.forEach(admin -> {
                telegramBot.sendMessage(
                        admin.getChatId(), NotificationTemplates.NO_EXPIRED_BOOKINGS_MESSAGE);
            });
        } else {
            expiringBookings.forEach(booking -> {
                sendNotification(booking.getUser().getId(), booking);
                sendNotification(booking.getAccommodation());
            });
        }
    }

    @Override
    public void sendNotification(Payment payment) {
        TelegramChat userChat = getChat(payment.getBooking().getUser().getId());
        String message = prepareNotification(payment);
        telegramBot.sendMessage(userChat.getChatId(), message);
    }

    private TelegramChat getChat(Long userId) {
        return telegramRepository.findByUserId(userId).orElseThrow(
                () -> new EntityNotFoundException(
                        "Can't fetch TelegramChat from DB by user id: " + userId));
    }

    private String prepareNotification(Payment payment) {
        switch (payment.getStatus().getStatus()) {
            case PAID -> {
                return String.format(
                        NotificationTemplates.PAYMENT_SUCCESSFUL_MESSAGE,
                        payment.getBooking().getUser().getFirstName(),
                        payment.getBooking().getUser().getLastName(),
                        payment.getBooking().getId(),
                        payment.getAmountToPay()
                        );
            }
            default -> {
                return String.format(
                        NotificationTemplates.PAYMENT_CANCELED_MESSAGE,
                        payment.getBooking().getUser().getFirstName(),
                        payment.getBooking().getUser().getLastName()
                );
            }
        }
    }

    private String prepareNotification(TelegramChat userChat, Booking booking) {
        StringBuilder notification = new StringBuilder();
        switch (booking.getStatus().getStatus()) {
            case PENDING -> notification.append(
                    NotificationTemplates.NOTIFICATION_PENDING_TEMPLATE);
            case CANCELLED -> notification.append(
                    NotificationTemplates.NOTIFICATION_CANCEL_TEMPLATE);
            case EXPIRED -> notification.append(
                    NotificationTemplates.NOTIFICATION_EXPIRED_TEMPLATE);
            default -> notification.append(
                    NotificationTemplates.NOTIFICATION_DEFAULT_TEMPLATE);
        }
        notification.append(NotificationTemplates.NOTIFICATION_BOOKING_DETAILS_TEMPLATE)
                .append(prepareNotification(booking.getAccommodation()));

        return String.format(notification.toString(),
                userChat.getUser().getFirstName(),
                userChat.getUser().getLastName(),
                booking.getStatus().getStatus(),
                booking.getCheckInDate(),
                booking.getCheckOutDate());
    }

    private String prepareNotification(Accommodation accommodation) {
        return String.format(NotificationTemplates.NOTIFICATION_ACCOMMODATION_DETAILS_TEMPLATE,
                accommodation.getType().getName(),
                accommodation.getSize(),
                getAmenitiesString(accommodation.getAmenities()),
                getAddressString(accommodation.getLocation()));
    }

    private String getAmenitiesString(Set<AmenityType> amenities) {
        return amenities.stream()
                .map(e -> e.getName().name())
                .collect(Collectors.joining(AMENITIES_DELIMITER));
    }

    private String getAddressString(Address address) {
        return String.format(NotificationTemplates.NOTIFICATION_ADDRESS_TEMPLATE,
                address.getAddress(),
                address.getCity(),
                address.getState(),
                address.getZipCode(),
                address.getCountry());
    }
}
