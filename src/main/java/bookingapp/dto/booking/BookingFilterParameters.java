package bookingapp.dto.booking;

import bookingapp.model.booking.BookingStatus;
import jakarta.validation.constraints.NotNull;

public record BookingFilterParameters(
        @NotNull
        Long userId,
        @NotNull
        BookingStatus.Status status
) {
}
