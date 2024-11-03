package bookingapp.dto.booking;

import bookingapp.validation.Date;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

@Date(startDate = "checkInDate", endDate = "checkOutDate")
public record BookingRequestDto(
        @NotNull
        Long accommodationId,
        @NotNull
        LocalDate checkInDate,
        @NotNull
        LocalDate checkOutDate
) {
}
