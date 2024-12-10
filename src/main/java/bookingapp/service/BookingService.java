package bookingapp.service;

import bookingapp.dto.booking.BookingFilterParameters;
import bookingapp.dto.booking.BookingRequestDto;
import bookingapp.dto.booking.BookingResponseDto;
import bookingapp.dto.booking.BookingUpdateRequestDto;
import bookingapp.model.user.User;
import java.util.List;
import org.springframework.data.domain.Pageable;

public interface BookingService {
    BookingResponseDto createBooking(User user, BookingRequestDto requestDto);

    List<BookingResponseDto> filter(BookingFilterParameters filterParameters, Pageable pageable);

    List<BookingResponseDto> getBookingsByUserId(Long userId, Pageable pageable);

    BookingResponseDto getBookingByIdAndUserId(Long id, Long userId);

    BookingResponseDto updateBooking(Long id, Long userId, BookingUpdateRequestDto requestDto);

    void cancelBooking(Long id, Long userId);

    void processExpiredBooking();
}
