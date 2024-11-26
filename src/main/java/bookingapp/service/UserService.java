package bookingapp.service;

import bookingapp.dto.user.UserRegistrationRequestDto;
import bookingapp.dto.user.UserResponseDto;
import bookingapp.dto.user.UserUpdateRequestDto;
import bookingapp.dto.user.UserUpdateRoleRequestDto;
import bookingapp.exception.RegistrationException;
import bookingapp.model.telegram.TelegramChat;

public interface UserService {
    UserResponseDto register(UserRegistrationRequestDto requestDto)
            throws RegistrationException;

    UserResponseDto updateRoleById(Long id, UserUpdateRoleRequestDto requestDto);

    UserResponseDto getInfo(Long id);

    UserResponseDto updateUser(Long id, UserUpdateRequestDto requestDto);

    TelegramChat subscribeToChat(Long chatId, String username);

    void unsubscribeFromChat(Long chatId);
}
