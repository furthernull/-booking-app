package bookingapp.dto.user;

import bookingapp.model.user.Role;
import jakarta.validation.constraints.NotNull;

public record UserUpdateRoleRequestDto(
        @NotNull
        Role.RoleName roleName
) {
}
