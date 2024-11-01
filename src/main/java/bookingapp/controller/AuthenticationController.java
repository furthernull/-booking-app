package bookingapp.controller;

import bookingapp.dto.user.UserLoginRequestDto;
import bookingapp.dto.user.UserLoginResponseDto;
import bookingapp.dto.user.UserRegistrationRequestDto;
import bookingapp.dto.user.UserResponseDto;
import bookingapp.exception.RegistrationException;
import bookingapp.security.AuthenticationService;
import bookingapp.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/auth")
public class AuthenticationController {
    private final UserService userService;
    private final AuthenticationService authenticationService;

    @PostMapping("/login")
    public UserLoginResponseDto loginUser(
            @RequestBody @Valid UserLoginRequestDto requestDto) {
        return authenticationService.authenticate(requestDto);
    }

    @PostMapping("/registration")
    public UserResponseDto registerUser(
            @RequestBody @Valid UserRegistrationRequestDto requestDto)
            throws RegistrationException {
        return userService.register(requestDto);
    }
}
