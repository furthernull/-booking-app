package bookingapp.service;

import static bookingapp.test.TestUtils.CUSTOMER_ENCODED_PASSWORD;
import static bookingapp.test.TestUtils.DEFAULT_ID_ONE;
import static bookingapp.test.TestUtils.DEFAULT_ID_SEVEN;
import static bookingapp.test.TestUtils.DEFAULT_ID_THREE;
import static bookingapp.test.TestUtils.DEFAULT_ID_TWO;
import static bookingapp.test.TestUtils.ROLE_ADMIN;
import static bookingapp.test.TestUtils.ROLE_CUSTOMER;
import static bookingapp.test.TestUtils.ROLE_NAME_ADMIN;
import static bookingapp.test.TestUtils.ROLE_NAME_CUSTOMER;
import static bookingapp.test.TestUtils.SUBSCRIBED_TELEGRAM_CHAT;
import static bookingapp.test.TestUtils.UNSUBSCRIBE_TELEGRAM_CHAT;
import static bookingapp.test.TestUtils.USER_ADMIN;
import static bookingapp.test.TestUtils.USER_ADMIN_RESPONSE_DTO;
import static bookingapp.test.TestUtils.USER_CUSTOMER;
import static bookingapp.test.TestUtils.USER_CUSTOMER_2;
import static bookingapp.test.TestUtils.USER_CUSTOMER_UPDATED_ROLE;
import static bookingapp.test.TestUtils.USER_REGISTRATION_REQUEST_DTO;
import static bookingapp.test.TestUtils.USER_RESPONSE_DTO;
import static bookingapp.test.TestUtils.USER_UPDATE_REQUEST_DTO;
import static bookingapp.test.TestUtils.USER_UPDATE_ROLE_REQUEST_DTO;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import bookingapp.dto.user.UserRegistrationRequestDto;
import bookingapp.dto.user.UserResponseDto;
import bookingapp.exception.EntityNotFoundException;
import bookingapp.exception.RegistrationException;
import bookingapp.mapper.UserMapper;
import bookingapp.model.telegram.TelegramChat;
import bookingapp.repository.role.RoleRepository;
import bookingapp.repository.telegram.TelegramRepository;
import bookingapp.repository.user.UserRepository;
import bookingapp.service.impl.UserServiceImpl;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {
    @Spy
    private PasswordEncoder passwordEncoder;
    @Mock
    private RoleRepository roleRepository;
    @Mock
    private TelegramRepository telegramRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private UserMapper userMapper;
    @InjectMocks
    private UserServiceImpl userServiceImpl;

    @Test
    @DisplayName("Verify register() method")
    void register_ValidRequest_ReturnsValidUserResponse() throws RegistrationException {
        UserRegistrationRequestDto requestDto = USER_REGISTRATION_REQUEST_DTO;

        when(userRepository.existsByEmail(requestDto.email())).thenReturn(false);
        when(userMapper.toModel(requestDto)).thenReturn(USER_CUSTOMER);
        when(passwordEncoder.encode(requestDto.password())).thenReturn(CUSTOMER_ENCODED_PASSWORD);
        when(roleRepository.findByRole(ROLE_NAME_CUSTOMER)).thenReturn(Optional.of(ROLE_CUSTOMER));
        when(userRepository.save(USER_CUSTOMER)).thenReturn(USER_CUSTOMER);
        when(userMapper.toDto(USER_CUSTOMER)).thenReturn(USER_RESPONSE_DTO);

        UserResponseDto actual = userServiceImpl.register(USER_REGISTRATION_REQUEST_DTO);

        assertEquals(USER_RESPONSE_DTO, actual);
    }

    @Test
    @DisplayName("Verify register() method, when user registered")
    void register_UserRegistered_ThrowException() throws RegistrationException {
        UserRegistrationRequestDto requestDto = USER_REGISTRATION_REQUEST_DTO;
        String expected = "User already exists";

        when(userRepository.existsByEmail(requestDto.email())).thenReturn(true);
        RegistrationException ex = assertThrows(RegistrationException.class,
                () -> userServiceImpl.register(USER_REGISTRATION_REQUEST_DTO));
        assertEquals(expected, ex.getMessage());
    }

    @Test
    @DisplayName("Verify updateRoleById() method")
    void updateRoleById_ValidUserId_ShouldUpdateRole() {
        when(userRepository.findById(DEFAULT_ID_TWO)).thenReturn(Optional.of(USER_CUSTOMER));
        when(roleRepository.findByRole(ROLE_NAME_ADMIN)).thenReturn(Optional.of(ROLE_ADMIN));
        when(userRepository.save(USER_CUSTOMER)).thenReturn(USER_CUSTOMER_UPDATED_ROLE);
        when(userMapper.toDto(USER_CUSTOMER)).thenReturn(USER_RESPONSE_DTO);

        UserResponseDto actual = userServiceImpl
                .updateRoleById(DEFAULT_ID_TWO, USER_UPDATE_ROLE_REQUEST_DTO);

        assertEquals(USER_RESPONSE_DTO, actual);
    }

    @Test
    @DisplayName("Verify getInfo() method")
    void getInfo_ValidId_ReturnsUserResponseDto() {
        when(userRepository.findById(DEFAULT_ID_ONE)).thenReturn(Optional.of(USER_ADMIN));
        when(userMapper.toDto(USER_ADMIN)).thenReturn(USER_ADMIN_RESPONSE_DTO);

        UserResponseDto actual = userServiceImpl.getInfo(DEFAULT_ID_ONE);
        assertEquals(USER_ADMIN_RESPONSE_DTO, actual);
    }

    @Test
    @DisplayName("Verify updateUser() method")
    void updateUser_ValidUserId_ShouldUpdateUser() {
        when(userRepository.findById(DEFAULT_ID_THREE)).thenReturn(Optional.of(USER_CUSTOMER_2));
        doNothing().when(userMapper).updateUser(USER_CUSTOMER_2, USER_UPDATE_REQUEST_DTO);
        when(userRepository.save(USER_CUSTOMER_2)).thenReturn(USER_CUSTOMER_2);
        when(userMapper.toDto(USER_CUSTOMER_2)).thenReturn(USER_RESPONSE_DTO);

        UserResponseDto actual = userServiceImpl
                .updateUser(DEFAULT_ID_THREE, USER_UPDATE_REQUEST_DTO);
        assertEquals(USER_RESPONSE_DTO, actual);
    }

    @Test
    @DisplayName("Verify subscribeToChat() method")
    void subscribeToChat_ValidChatIdAndUsername_ShouldSubscribeToChat() {
        Long chatId = DEFAULT_ID_SEVEN;
        String username = USER_CUSTOMER.getUsername();

        when(userRepository.findByEmail(username)).thenReturn(Optional.of(USER_CUSTOMER));
        when(telegramRepository.findByChatId(chatId)).thenReturn(Optional.empty());
        when(telegramRepository.save(any(TelegramChat.class))).thenReturn(SUBSCRIBED_TELEGRAM_CHAT);

        TelegramChat actual = userServiceImpl.subscribeToChat(chatId, username);

        assertTrue(actual.isSubscribed());
        assertEquals(chatId, actual.getChatId());
        assertEquals(USER_CUSTOMER, actual.getUser());
    }

    @Test
    @DisplayName("Verify subscribeToChat() method with existing chat")
    void subscribeToChat_ExistingChat_ShouldUpdateSubscription() {
        Long chatId = DEFAULT_ID_SEVEN;
        String username = USER_CUSTOMER.getUsername();

        when(userRepository.findByEmail(username)).thenReturn(Optional.of(USER_CUSTOMER));
        when(telegramRepository.findByChatId(chatId))
                .thenReturn(Optional.of(SUBSCRIBED_TELEGRAM_CHAT));
        when(telegramRepository.save(SUBSCRIBED_TELEGRAM_CHAT))
                .thenReturn(SUBSCRIBED_TELEGRAM_CHAT);

        TelegramChat actual = userServiceImpl.subscribeToChat(chatId, username);

        assertTrue(actual.isSubscribed());
        assertEquals(chatId, actual.getChatId());
        assertEquals(USER_CUSTOMER, actual.getUser());
    }

    @Test
    @DisplayName("Verify unsubscribeFromChat() method")
    void unsubscribeFromChat_ValidChatId_ShouldUnsubscribeFromChat() {
        Long chatId = DEFAULT_ID_SEVEN;

        when(telegramRepository.findByChatId(chatId))
                .thenReturn(Optional.of(SUBSCRIBED_TELEGRAM_CHAT));
        when(telegramRepository.save(SUBSCRIBED_TELEGRAM_CHAT))
                .thenReturn(UNSUBSCRIBE_TELEGRAM_CHAT);

        userServiceImpl.unsubscribeFromChat(chatId);

        verify(telegramRepository).save(SUBSCRIBED_TELEGRAM_CHAT);
    }

    @Test
    @DisplayName("Verify unsubscribeFromChat() method with invalid chatId")
    void unsubscribeFromChat_InvalidChatId_ShouldThrowException() {
        Long chatId = 100L;
        String expected = "Can't retrieve telegram chat by id: " + chatId;

        when(telegramRepository.findByChatId(chatId)).thenReturn(Optional.empty());

        EntityNotFoundException ex = assertThrows(EntityNotFoundException.class,
                () -> userServiceImpl.unsubscribeFromChat(chatId));
        assertEquals(expected, ex.getMessage());
    }
}
