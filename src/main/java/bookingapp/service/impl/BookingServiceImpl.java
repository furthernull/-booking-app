package bookingapp.service.impl;

import bookingapp.dto.booking.BookingFilterParameters;
import bookingapp.dto.booking.BookingRequestDto;
import bookingapp.dto.booking.BookingResponseDto;
import bookingapp.dto.booking.BookingUpdateRequestDto;
import bookingapp.exception.AccommodationAvailabilityException;
import bookingapp.exception.EntityNotFoundException;
import bookingapp.exception.IllegalStateBookingException;
import bookingapp.mapper.BookingMapper;
import bookingapp.model.accommodation.Accommodation;
import bookingapp.model.booking.Booking;
import bookingapp.model.booking.BookingStatus;
import bookingapp.model.user.User;
import bookingapp.repository.accommodation.AccommodationRepository;
import bookingapp.repository.booking.BookingRepository;
import bookingapp.repository.booking.BookingSpecificationBuilder;
import bookingapp.repository.bookingstatus.BookingStatusRepository;
import bookingapp.service.BookingService;
import bookingapp.service.NotificationService;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class BookingServiceImpl implements BookingService {
    private static final BookingStatus.Status PENDING_STATUS = BookingStatus.Status.PENDING;
    private static final BookingStatus.Status CANCELLED_STATUS = BookingStatus.Status.CANCELLED;
    private final AccommodationRepository accommodationRepository;
    private final BookingMapper bookingMapper;
    private final BookingRepository bookingRepository;
    private final BookingStatusRepository bookingStatusRepository;
    private final BookingSpecificationBuilder bookingSpecificationBuilder;
    private final NotificationService notificationService;

    @Transactional
    @Override
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
        booking.setAccommodation(fetchAccommodation(requestDto.accommodationId()));
        booking.setUser(user);
        BookingStatus status = bookingStatusRepository.findByStatus(
                PENDING_STATUS).orElseThrow(
                        () -> new EntityNotFoundException(
                                "Can't retrieve status PENDING from DB"));
        booking.setStatus(status);
        bookingRepository.save(booking);
        notificationService.sendNotification(user.getId(), booking);
        return bookingMapper.toDto(booking);
    }

    @Override
    public List<BookingResponseDto> filter(
            BookingFilterParameters filterParameters,
            Pageable pageable) {
        Specification<Booking> bookingSpecification
                = bookingSpecificationBuilder.build(filterParameters);
        Page<Booking> bookings = bookingRepository.findAll(bookingSpecification, pageable);
        return bookingMapper.toDto(bookings);
    }

    @Override
    public List<BookingResponseDto> getBookingsByUserId(Long userId, Pageable pageable) {
        List<Booking> bookings = bookingRepository.findAllByUserId(userId, pageable);
        return bookingMapper.toDto(bookings);
    }

    @Override
    public BookingResponseDto getBookingByIdAndUserId(Long id, Long userId) {
        Booking booking = bookingRepository.findByIdAndUserId(id, userId).orElseThrow(
                () -> new EntityNotFoundException("Can't find Booking with id " + id)
        );
        return bookingMapper.toDto(booking);
    }

    @Override
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
    public void cancelBooking(Long id, Long userId) {
        Booking booking = bookingRepository.findByIdAndUserId(id, userId).orElseThrow(
                () -> new EntityNotFoundException("Can't cancel Booking with id " + id)
        );
        if (booking.getStatus().getStatus().equals(CANCELLED_STATUS)) {
            throw new IllegalStateBookingException("Booking has already been canceled");
        }
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

    private Accommodation fetchAccommodation(Long accommodationId) {
        return accommodationRepository.getReferenceById(accommodationId);
    }
}
