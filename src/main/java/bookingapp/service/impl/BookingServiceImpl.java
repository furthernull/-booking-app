package bookingapp.service.impl;

import bookingapp.dto.booking.BookingRequestDto;
import bookingapp.dto.booking.BookingResponseDto;
import bookingapp.dto.booking.BookingUpdateRequestDto;
import bookingapp.exception.AccommodationAvailabilityException;
import bookingapp.exception.EntityNotFoundException;
import bookingapp.mapper.BookingMapper;
import bookingapp.model.booking.Booking;
import bookingapp.model.booking.BookingStatus;
import bookingapp.model.user.User;
import bookingapp.repository.booking.BookingRepository;
import bookingapp.repository.bookinstatus.BookingStatusRepository;
import bookingapp.service.BookingService;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class BookingServiceImpl implements BookingService {
    private static final BookingStatus.Status PENDING_STATUS = BookingStatus.Status.PENDING;
    private static final BookingStatus.Status CANCELLED_STATUS = BookingStatus.Status.CANCELLED;
    private final BookingRepository bookingRepository;
    private final BookingMapper bookingMapper;
    private final BookingStatusRepository bookingStatusRepository;

    @Override
    @Transactional
    public BookingResponseDto createBooking(User user, BookingRequestDto requestDto) {
        if (!isAccommodationAvailable(
                requestDto.accommodationId(),
                requestDto.checkInDate(),
                requestDto.checkOutDate())
        ) {
            throw new AccommodationAvailabilityException(
                    "Unavailable for selected dates: " + requestDto.checkInDate()
                            + " - " + requestDto.checkOutDate());
        }
        Booking booking = bookingMapper.toModel(requestDto);
        booking.setUser(user);
        BookingStatus status = bookingStatusRepository.findByStatus(
                PENDING_STATUS).orElseThrow(
                        () -> new EntityNotFoundException(
                                "Can't retrieve status PENDING from DB"));
        booking.setStatus(status);
        bookingRepository.save(booking);
        return bookingMapper.toDto(booking);
    }

    @Override
    public List<BookingResponseDto> getAll(
            Pageable pageable) {
        Page<Booking> bookings = bookingRepository.findAll(pageable);
        return bookingMapper.toDto(bookings);
    }

    @Override
    public List<BookingResponseDto> getBookingsByUserId(Long userId, Pageable pageable) {
        List<Booking> bookings = bookingRepository.findAllByUserId(userId, pageable);
        return bookingMapper.toDto(bookings);
    }

    @Override
    public BookingResponseDto getBookingById(Long id) {
        Booking booking = bookingRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Can't find Booking with id " + id)
        );
        return bookingMapper.toDto(booking);
    }

    @Override
    @Transactional
    public BookingResponseDto updateBooking(
            Long id, Long userId,
            BookingUpdateRequestDto requestDto
    ) {
        Booking booking = bookingRepository.findByIdAndUserId(id, userId).orElseThrow(
                () -> new EntityNotFoundException("Can't update Booking with id " + id)
        );
        if (!isAccommodationAvailableForUpdate(
                id,
                booking.getAccommodation().getId(),
                requestDto.checkInDate(),
                requestDto.checkOutDate())
        ) {
            throw new AccommodationAvailabilityException(
                    "Accommodation is not available for selected dates");
        }
        bookingMapper.updateBooking(booking, requestDto);
        return bookingMapper.toDto(bookingRepository.save(booking));
    }

    @Override
    @Transactional
    public void cancelBooking(Long id, Long userId) {
        Booking booking = bookingRepository.findByIdAndUserId(id, userId).orElseThrow(
                () -> new EntityNotFoundException("Can't delete Booking with id " + id)
        );
        BookingStatus status = bookingStatusRepository.findByStatus(CANCELLED_STATUS)
                .orElseThrow(
                        () -> new EntityNotFoundException("Can't retrieve Status CANCELED")
        );
        booking.setStatus(status);
        bookingRepository.save(booking);
    }

    private boolean isAccommodationAvailable(
            Long accommodationId,
            LocalDate startDate,
            LocalDate endDate
    ) {
        return bookingRepository
                .findConflictingBooking(accommodationId, startDate, endDate, CANCELLED_STATUS)
                .stream()
                .findAny()
                .isEmpty();
    }

    private boolean isAccommodationAvailableForUpdate(
            Long id,
            Long accommodationId,
            LocalDate startDate,
            LocalDate endDate
    ) {
        return bookingRepository
                .findConflictingBooking(accommodationId, startDate, endDate, CANCELLED_STATUS)
                .stream()
                .filter(b -> !b.getId().equals(id))
                .findAny()
                .isEmpty();
    }
}
