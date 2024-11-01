package bookingapp.mapper;

import bookingapp.config.MapperConfig;
import bookingapp.dto.user.UserRegistrationRequestDto;
import bookingapp.dto.user.UserResponseDto;
import bookingapp.dto.user.UserUpdateRequestDto;
import bookingapp.model.user.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(config = MapperConfig.class)
public interface UserMapper {
    UserResponseDto toDto(User user);

    @Mapping(target = "password", ignore = true)
    User toModel(UserRegistrationRequestDto requestDto);

    @Mapping(target = "email", nullValuePropertyMappingStrategy
            = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "firstName", nullValuePropertyMappingStrategy
            = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "lastName", nullValuePropertyMappingStrategy
            = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "password", ignore = true)
    void updateUser(@MappingTarget User user, UserUpdateRequestDto requestDto);
}
