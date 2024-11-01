package bookingapp.service.impl;

import bookingapp.dto.user.UserRegistrationRequestDto;
import bookingapp.dto.user.UserResponseDto;
import bookingapp.dto.user.UserUpdateRequestDto;
import bookingapp.dto.user.UserUpdateRoleRequestDto;
import bookingapp.exception.EntityNotFoundException;
import bookingapp.exception.RegistrationException;
import bookingapp.mapper.UserMapper;
import bookingapp.model.user.Role;
import bookingapp.model.user.User;
import bookingapp.repository.role.RoleRepository;
import bookingapp.repository.user.UserRepository;
import bookingapp.service.UserService;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;

    @Override
    public UserResponseDto register(UserRegistrationRequestDto requestDto)
            throws RegistrationException {
        if (userRepository.existsByEmail(requestDto.email())) {
            throw new RegistrationException("User already exists");
        }
        User user = userMapper.toModel(requestDto);
        user.setPassword(passwordEncoder.encode(requestDto.password()));
        Role role = roleRepository.findByRole(Role.RoleName.CUSTOMER).orElseThrow(
                () -> new RegistrationException("Invalid role")
        );
        user.setRoles(Set.of(role));
        userRepository.save(user);
        return userMapper.toDto(user);
    }

    @Override
    public UserResponseDto updateRoleById(Long id, UserUpdateRoleRequestDto requestDto) {
        User user = userRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Can't update role by id: " + id)
        );
        Role role = roleRepository.findByRole(requestDto.roleName()).orElseThrow(
                () -> new EntityNotFoundException("Can't retrieve role by name: "
                        + requestDto.roleName())
        );
        user.setRoles(Set.of(role));
        userRepository.save(user);
        return userMapper.toDto(user);
    }

    @Override
    public UserResponseDto getInfo(Long id) {
        User user = userRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Can't retrieve user by id: " + id));
        return userMapper.toDto(user);
    }

    @Override
    public UserResponseDto updateUser(Long id, UserUpdateRequestDto requestDto) {
        User user = userRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Can't update user by id: " + id)
        );
        userMapper.updateUser(user, requestDto);
        user.setPassword(passwordEncoder.encode(requestDto.password()));
        userRepository.save(user);
        return userMapper.toDto(user);
    }
}
