package bookingapp.dto.accommodation;

import bookingapp.dto.address.AddressRequestDto;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;
import java.util.Set;

public record AccommodationRequestDto(
        @NotNull
        @Positive
        Long accommodationTypeId,
        @NotNull
        AddressRequestDto address,
        @NotBlank
        String size,
        @NotNull
        Set<Long> amenityIds,
        @NotNull
        @Positive
        BigDecimal dailyRate,
        @NotNull
        @Positive
        Integer availability
) {
}
