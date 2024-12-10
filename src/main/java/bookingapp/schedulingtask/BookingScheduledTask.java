package bookingapp.schedulingtask;

import bookingapp.service.BookingService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class BookingScheduledTask {
    private final BookingService bookingService;

    @Scheduled(cron = "${cron.expression}")
    public void scheduleExpiredBookings() {
        bookingService.processExpiredBooking();
    }
}
