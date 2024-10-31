package bookingapp.dto.address;

import jakarta.validation.constraints.NotBlank;

public record AddressRequestDto(
        @NotBlank
        String address,
        @NotBlank
        String city,
        String state,
        @NotBlank
        String zip,
        @NotBlank
        String country
) {
}
