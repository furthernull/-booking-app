package bookingapp.mapper;

import bookingapp.config.MapperConfig;
import bookingapp.dto.booking.BookingRequestDto;
import bookingapp.dto.booking.BookingResponseDto;
import bookingapp.dto.booking.BookingUpdateRequestDto;
import bookingapp.model.booking.Booking;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(config = MapperConfig.class)
public interface BookingMapper {
    @Mapping(target = "accommodation", ignore = true)
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "status", ignore = true)
    Booking toModel(BookingRequestDto requestDto);

    @Mapping(target = "accommodationId", source = "accommodation.id")
    @Mapping(target = "customerId", source = "user.id")
    @Mapping(target = "bookingStatusId", source = "status.id")
    BookingResponseDto toDto(Booking booking);

    List<BookingResponseDto> toDto(Iterable<Booking> bookings);

    void updateBooking(@MappingTarget Booking booking, BookingUpdateRequestDto requestDto);
}
