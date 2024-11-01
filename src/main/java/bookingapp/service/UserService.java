package bookingapp.service;

import bookingapp.dto.user.UserRegistrationRequestDto;
import bookingapp.dto.user.UserResponseDto;
import bookingapp.dto.user.UserUpdateRequestDto;
import bookingapp.dto.user.UserUpdateRoleRequestDto;
import bookingapp.exception.RegistrationException;

public interface UserService {
    UserResponseDto register(UserRegistrationRequestDto requestDto)
            throws RegistrationException;

    UserResponseDto updateRoleById(Long id, UserUpdateRoleRequestDto requestDto);

    UserResponseDto getInfo(Long id);

    UserResponseDto updateUser(Long id, UserUpdateRequestDto requestDto);
}
