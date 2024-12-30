package bookingapp.telegram;

public class NotificationTemplates {
    public static final String START_COMMAND_TEMPLATE = "Hi, %s! Want to observe bookings?\n"
            + "Please text with /enroll command\nand provide your username from bookingapp.\n"
            + "Provide in the next format: \n\"/enroll username\"";
    public static final String ENROLL_COMMAND_TEMPLATE = "You are successfully enrolled!\n"
            + "chat id: %s,\nemail: %s\n";
    public static final String STOP_COMMAND_TEMPLATE = "Notifications silenced, for user: %s";
    public static final String COMMAND_NOT_RECOGNIZED_TEXT = "Command not recognized";
    public static final String NOTIFICATION_PENDING_TEMPLATE
            = "Thank you for your booking, %s %s!\n\n";
    public static final String NOTIFICATION_CANCEL_TEMPLATE
            = "%s %s, you booking was cancelled!\n\nBooking details:\n";
    public static final String NOTIFICATION_EXPIRED_TEMPLATE
            = "%s %s, you booking will expiring tomorrow!\n\n";
    public static final String NOTIFICATION_DEFAULT_TEMPLATE
            = "%s, %s!\n\n";
    public static final String NOTIFICATION_BOOKING_DETAILS_TEMPLATE
            = "Booking details:\nStatus: %s\nCheck-in: %s\nCheck-out: %s\n\n";
    public static final String NOTIFICATION_ACCOMMODATION_DETAILS_TEMPLATE
            = "Accommodation details:\n%s, %s\nAmenities: %s\nLocation:\n%s";
    public static final String NOTIFICATION_ADDRESS_TEMPLATE = "%s,\n%s, %s %s, %s";
    public static final String NO_EXPIRED_BOOKINGS_MESSAGE = "No expired bookings today!";
    public static final String PAYMENT_SUCCESSFUL_MESSAGE = "Thanks for your order, %s %s!\n"
            + "Booking id #%s for $%s has confirmed.";
    public static final String PAYMENT_CANCELED_MESSAGE = "%s %s, your payment was cancelled,\n"
            + "your payment can be made later(but the session is available for only 24 hours)";
}
