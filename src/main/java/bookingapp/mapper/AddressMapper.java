package bookingapp.mapper;

import bookingapp.config.MapperConfig;
import bookingapp.dto.address.AddressRequestDto;
import bookingapp.model.accommodation.Address;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(config = MapperConfig.class)
public interface AddressMapper {
    @Mapping(target = "zipCode", source = "zip")
    Address toModel(AddressRequestDto requestDto);

    @Mapping(target = "zipCode", source = "zip")
    void updateModel(@MappingTarget Address address, AddressRequestDto requestDto);
}
