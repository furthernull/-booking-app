package bookingapp.controller;

import bookingapp.dto.user.UserResponseDto;
import bookingapp.dto.user.UserUpdateRequestDto;
import bookingapp.dto.user.UserUpdateRoleRequestDto;
import bookingapp.model.user.User;
import bookingapp.service.UserService;
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

@RequiredArgsConstructor
@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    @PreAuthorize("hasAuthority('ADMIN')")
    @PutMapping("/{id}/role")
    public UserResponseDto updateRole(
            @PathVariable Long id,
            @Valid @RequestBody UserUpdateRoleRequestDto requestDto
    ) {
        return userService.updateRoleById(id, requestDto);
    }

    @PreAuthorize("hasAuthority('CUSTOMER')")
    @GetMapping("/me")
    public UserResponseDto getInfo(@AuthenticationPrincipal User user) {
        return userService.getInfo(user.getId());
    }

    @PreAuthorize("hasAuthority('CUSTOMER')")
    @PutMapping("/me")
    public UserResponseDto updateUser(
            @AuthenticationPrincipal User user,
            @Valid @RequestBody UserUpdateRequestDto requestDto
    ) {
        return userService.updateUser(user.getId(), requestDto);
    }
}
