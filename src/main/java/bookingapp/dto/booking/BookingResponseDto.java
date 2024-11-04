package bookingapp.dto.booking;

import java.time.LocalDate;

public record BookingResponseDto(
        Long id,
        LocalDate checkInDate,
        LocalDate checkOutDate,
        Long accommodationId,
        Long customerId,
        Long bookingStatusId
) {
}
