package bookingapp.mapper;

import bookingapp.config.MapperConfig;
import bookingapp.dto.accommodation.AccommodationDto;
import bookingapp.dto.accommodation.AccommodationRequestDto;
import bookingapp.model.accommodation.Accommodation;
import bookingapp.model.accommodation.AmenityType;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(config = MapperConfig.class, uses = AddressMapper.class)
public interface AccommodationMapper {
    @Mapping(target = "type", source = "type.id")
    @Mapping(target = "location", source = "location.id")
    @Mapping(target = "amenityIds", ignore = true)
    AccommodationDto toDto(Accommodation accommodation);

    List<AccommodationDto> toDto(Iterable<Accommodation> accommodations);

    @AfterMapping
    default void setAmenityIds(
            @MappingTarget AccommodationDto accommodationDto,
            Accommodation accommodation
    ) {
        Set<Long> ids = accommodation.getAmenities().stream()
                .map(AmenityType::getId)
                .collect(Collectors.toSet());
        accommodationDto.setAmenityIds(ids);
    }

    @Mapping(source = "accommodationTypeId", target = "type.id")
    @Mapping(source = "address", target = "location")
    @Mapping(target = "amenities", ignore = true)
    Accommodation toModel(AccommodationRequestDto requestDto);

    @Mapping(target = "type.id", source = "accommodationTypeId")
    @Mapping(target = "amenities", ignore = true)
    @Mapping(target = "location", source = "address")
    void updateModel(@MappingTarget Accommodation accommodation,
                     AccommodationRequestDto requestDto);
}
