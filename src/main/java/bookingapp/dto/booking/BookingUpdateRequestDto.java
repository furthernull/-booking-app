package bookingapp.dto.booking;

import bookingapp.validation.Date;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

@Date(startDate = "checkInDate", endDate = "checkOutDate")
public record BookingUpdateRequestDto(
        @NotNull
        LocalDate checkInDate,
        @NotNull
        LocalDate checkOutDate
) {
}
