package bookingapp.controller;

import bookingapp.dto.user.UserResponseDto;
import bookingapp.dto.user.UserUpdateRequestDto;
import bookingapp.dto.user.UserUpdateRoleRequestDto;
import bookingapp.model.user.User;
import bookingapp.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "User management",
        description = "Managing authentication and user registration")
@RequiredArgsConstructor
@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    @PreAuthorize("hasAuthority('ADMIN')")
    @PutMapping("/{id}/role")
    @Operation(summary = "Update role",
            description = "enables users to update their roles, providing role-based access")
    public UserResponseDto updateRole(
            @PathVariable Long id,
            @Valid @RequestBody UserUpdateRoleRequestDto requestDto
    ) {
        return userService.updateRoleById(id, requestDto);
    }

    @PreAuthorize("hasAuthority('CUSTOMER')")
    @GetMapping("/me")
    @Operation(summary = "Get info",
            description = "retrieves the profile information for the currently logged-in user")
    public UserResponseDto getInfo(@AuthenticationPrincipal User user) {
        return userService.getInfo(user.getId());
    }

    @PreAuthorize("hasAuthority('CUSTOMER')")
    @PutMapping("/me")
    @Operation(summary = "Update user",
            description = "allows users to update their profile information")
    public UserResponseDto updateUser(
            @AuthenticationPrincipal User user,
            @Valid @RequestBody UserUpdateRequestDto requestDto
    ) {
        return userService.updateUser(user.getId(), requestDto);
    }
}
