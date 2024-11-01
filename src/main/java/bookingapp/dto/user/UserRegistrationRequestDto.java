package bookingapp.dto.user;

import bookingapp.validation.Email;
import bookingapp.validation.FieldMatches;
import bookingapp.validation.Password;
import jakarta.validation.constraints.NotBlank;

@FieldMatches(field = "password", fieldMatch = "passwordConfirmation")
public record UserRegistrationRequestDto(
        @NotBlank
        @Email
        String email,
        @NotBlank
        String firstName,
        @NotBlank
        String lastName,
        @NotBlank
        @Password
        String password,
        @NotBlank
        @Password
        String passwordConfirmation
) {
}
