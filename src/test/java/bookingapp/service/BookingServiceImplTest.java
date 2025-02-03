package bookingapp.service;

import static bookingapp.test.TestUtils.ACCOMMODATION_STUDIO;
import static bookingapp.test.TestUtils.BOOKING_CONDO_EXPIRED;
import static bookingapp.test.TestUtils.BOOKING_FILTER_PARAMETERS;
import static bookingapp.test.TestUtils.BOOKING_LIST;
import static bookingapp.test.TestUtils.BOOKING_PAGE;
import static bookingapp.test.TestUtils.BOOKING_STATUS_CANCELLED;
import static bookingapp.test.TestUtils.BOOKING_STATUS_EXPIRED;
import static bookingapp.test.TestUtils.BOOKING_STATUS_PENDING;
import static bookingapp.test.TestUtils.BOOKING_STUDIO_CANCELED;
import static bookingapp.test.TestUtils.BOOKING_STUDIO_PENDING;
import static bookingapp.test.TestUtils.BOOKING_STUDIO_REQUEST_DTO;
import static bookingapp.test.TestUtils.BOOKING_STUDIO_RESPONSE_DTO;
import static bookingapp.test.TestUtils.BOOKING_STUDIO_UPDATED;
import static bookingapp.test.TestUtils.BOOKING_STUDIO_UPDATED_RESPONSE_DTO;
import static bookingapp.test.TestUtils.BOOKING_UPDATE_REQUEST_DTO;
import static bookingapp.test.TestUtils.CONFLICTING_BOOKING;
import static bookingapp.test.TestUtils.PAGEABLE;
import static bookingapp.test.TestUtils.STATUS_CANCELLED;
import static bookingapp.test.TestUtils.STATUS_EXPIRED;
import static bookingapp.test.TestUtils.STATUS_PENDING;
import static bookingapp.test.TestUtils.USER_CUSTOMER;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import bookingapp.dto.booking.BookingRequestDto;
import bookingapp.dto.booking.BookingResponseDto;
import bookingapp.exception.AccommodationAvailabilityException;
import bookingapp.exception.EntityNotFoundException;
import bookingapp.mapper.BookingMapper;
import bookingapp.model.booking.Booking;
import bookingapp.model.booking.BookingStatus;
import bookingapp.repository.accommodation.AccommodationRepository;
import bookingapp.repository.booking.BookingRepository;
import bookingapp.repository.booking.BookingSpecificationBuilder;
import bookingapp.repository.bookingstatus.BookingStatusRepository;
import bookingapp.service.impl.BookingServiceImpl;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.jpa.domain.Specification;

@ExtendWith(MockitoExtension.class)
class BookingServiceImplTest {
    @Mock
    private AccommodationRepository accommodationRepository;
    @Mock
    private BookingMapper bookingMapper;
    @Mock
    private BookingRepository bookingRepository;
    @Mock
    private BookingStatusRepository bookingStatusRepository;
    @Mock
    private BookingSpecificationBuilder bookingSpecificationBuilder;
    @Mock
    private NotificationService notificationService;
    @InjectMocks
    private BookingServiceImpl bookingService;

    @Test
    @DisplayName("Verify createBooking() method")
    void createBooking_ValidUserAndBookingRequest_ReturnValidBookingResponse() {
        BookingRequestDto bookingRequestDto = BOOKING_STUDIO_REQUEST_DTO;
        Booking booking = BOOKING_STUDIO_PENDING;
        BookingResponseDto expected = BOOKING_STUDIO_RESPONSE_DTO;

        when(bookingRepository.findConflictingBooking(
                bookingRequestDto.accommodationId(),
                bookingRequestDto.checkInDate(),
                bookingRequestDto.checkOutDate(),
                STATUS_CANCELLED
        )).thenReturn(List.of());
        when(bookingMapper.toModel(bookingRequestDto)).thenReturn(booking);
        when(accommodationRepository.getReferenceById(
                bookingRequestDto.accommodationId())).thenReturn(ACCOMMODATION_STUDIO);
        when(bookingStatusRepository.findByStatus(STATUS_PENDING))
                .thenReturn(Optional.of(BOOKING_STATUS_PENDING));
        when(bookingRepository.save(booking)).thenReturn(booking);
        doNothing().when(notificationService).sendNotification(USER_CUSTOMER.getId(), booking);
        when(bookingMapper.toDto(booking)).thenReturn(expected);

        BookingResponseDto actual = bookingService.createBooking(USER_CUSTOMER, bookingRequestDto);
        assertNotNull(actual);
        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("Verify createBooking() method when accommodation unavailable")
    void createBooking_UnavailableDates_ShouldThrowException() {
        BookingRequestDto bookingRequestDto = BOOKING_STUDIO_REQUEST_DTO;

        when(bookingRepository.findConflictingBooking(
                bookingRequestDto.accommodationId(),
                bookingRequestDto.checkInDate(),
                bookingRequestDto.checkOutDate(),
                BookingStatus.Status.CANCELLED
        )).thenReturn(List.of(BOOKING_STUDIO_PENDING));
        AccommodationAvailabilityException ex = assertThrows(
                AccommodationAvailabilityException.class,
                () -> bookingService.createBooking(USER_CUSTOMER, bookingRequestDto));
        String expected = "Unavailable for selected dates: " + bookingRequestDto.checkInDate()
                + " - " + bookingRequestDto.checkOutDate();
        assertEquals(expected, ex.getMessage());
    }

    @Test
    @DisplayName("Verify filter() method")
    void filter_ValidParams_ReturnValidBookingResponse() {
        Specification<Booking> bookingSpecification = mock(Specification.class);

        when(bookingSpecificationBuilder.build(BOOKING_FILTER_PARAMETERS))
                .thenReturn(bookingSpecification);
        when(bookingRepository.findAll(bookingSpecification, PAGEABLE)).thenReturn(BOOKING_PAGE);
        when(bookingMapper.toDto(BOOKING_PAGE)).thenReturn(List.of(BOOKING_STUDIO_RESPONSE_DTO));

        List<BookingResponseDto> actual =
                bookingService.filter(BOOKING_FILTER_PARAMETERS, PAGEABLE);
        assertNotNull(actual);
        assertEquals(1, actual.size());
        assertEquals(BOOKING_STUDIO_RESPONSE_DTO, actual.get(0));
    }

    @Test
    @DisplayName("Verify getBookingsByUserId() method")
    void getBookingsByUserId_ValidUserId_ReturnValidBookingResponseList() {
        Long userId = 1L;

        when(bookingRepository.findAllByUserId(userId, PAGEABLE)).thenReturn(BOOKING_LIST);
        when(bookingMapper.toDto(BOOKING_LIST)).thenReturn(List.of(BOOKING_STUDIO_RESPONSE_DTO));

        List<BookingResponseDto> actual =
                bookingService.getBookingsByUserId(userId, PAGEABLE);
        assertNotNull(actual);
        assertFalse(actual.isEmpty());
        assertEquals(BOOKING_STUDIO_RESPONSE_DTO, actual.get(0));
    }

    @Test
    @DisplayName("Verify getBookingsByIdAndUserId() method")
    void getBookingByIdAndUserId_ValidIdAndUserId_ReturnValidBookingResponseList() {
        Long bookingId = 1L;
        Long userId = 1L;
        Booking booking = BOOKING_STUDIO_PENDING;

        when(bookingRepository.findByIdAndUserId(bookingId, userId))
                .thenReturn(Optional.of(booking));
        when(bookingMapper.toDto(booking)).thenReturn(BOOKING_STUDIO_RESPONSE_DTO);

        BookingResponseDto actual = bookingService.getBookingByIdAndUserId(bookingId, userId);
        assertNotNull(actual);
        assertEquals(BOOKING_STUDIO_RESPONSE_DTO, actual);
    }

    @Test
    @DisplayName("Verify getBookingsByIdAndUserId() method with invalid id")
    void getBookingsByIdAndUserId_NotValidIdValidUserId_ShouldThrowException() {
        Long bookingId = -1L;
        Long userId = 1L;
        String expected = "Can't find Booking with id " + bookingId;

        when(bookingRepository.findByIdAndUserId(bookingId, userId)).thenReturn(Optional.empty());
        EntityNotFoundException ex = assertThrows(EntityNotFoundException.class,
                () -> bookingService.getBookingByIdAndUserId(bookingId, userId));
        assertEquals(expected, ex.getMessage());
    }

    @Test
    @DisplayName("Verify updateBooking() method")
    void updateBooking_ValidBookingAndUserId_ReturnUpdatedBookingResponse() {
        Long bookingId = 1L;
        Long userId = 1L;
        Booking booking = BOOKING_STUDIO_PENDING;
        Booking updatedBooking = BOOKING_STUDIO_UPDATED;
        BookingResponseDto bookingResponseDto = BOOKING_STUDIO_UPDATED_RESPONSE_DTO;

        when(bookingRepository.findByIdAndUserId(bookingId, userId))
                .thenReturn(Optional.of(booking));
        doNothing().when(bookingMapper).updateBooking(booking, BOOKING_UPDATE_REQUEST_DTO);
        when(bookingRepository.save(booking)).thenReturn(updatedBooking);
        when(bookingMapper.toDto(updatedBooking)).thenReturn(bookingResponseDto);

        BookingResponseDto actual =
                bookingService.updateBooking(bookingId, userId, BOOKING_UPDATE_REQUEST_DTO);
        assertNotNull(actual);
        assertEquals(bookingResponseDto, actual);
    }

    @Test
    @DisplayName("Verify updateBooking() method with not valid booking id")
    void updateBooking_NotValidBookingId_ShouldThrowException() {
        Long bookingId = -1L;
        Long userId = 1L;
        String expected = "Can't update Booking with id " + bookingId;

        when(bookingRepository.findByIdAndUserId(bookingId, userId))
                .thenReturn(Optional.empty());
        EntityNotFoundException ex = assertThrows(EntityNotFoundException.class,
                () -> bookingService.updateBooking(bookingId, userId, BOOKING_UPDATE_REQUEST_DTO));
        assertEquals(expected, ex.getMessage());
    }

    @Test
    @DisplayName("Verify updateBooking() method when new dates are unavailable")
    void updateBooking_NewDatesUnavailable_ShouldThrowException() {
        Long bookingId = 1L;
        Long userId = 1L;
        String expected = "Accommodation is not available for selected dates";

        when(bookingRepository.findByIdAndUserId(bookingId, userId))
                .thenReturn(Optional.of(BOOKING_STUDIO_PENDING));
        when(bookingRepository.findConflictingBooking(
                BOOKING_STUDIO_PENDING.getAccommodation().getId(),
                BOOKING_UPDATE_REQUEST_DTO.checkInDate(),
                BOOKING_UPDATE_REQUEST_DTO.checkOutDate(),
                STATUS_CANCELLED
        )).thenReturn(List.of(CONFLICTING_BOOKING));

        AccommodationAvailabilityException exception = assertThrows(
                AccommodationAvailabilityException.class,
                () -> bookingService.updateBooking(bookingId, userId, BOOKING_UPDATE_REQUEST_DTO)
        );
        assertEquals(expected, exception.getMessage());
    }

    @Test
    @DisplayName("Verify cancelBooking() method")
    void cancelBooking_ValidBookingIdAndUserId_CancelBooking() {
        Long bookingId = 1L;
        Long userId = 1L;
        Booking booking = BOOKING_STUDIO_PENDING;

        when(bookingRepository.findByIdAndUserId(bookingId, userId))
                .thenReturn(Optional.of(booking));
        when(bookingStatusRepository.findByStatus(STATUS_CANCELLED))
                .thenReturn(Optional.of(BOOKING_STATUS_CANCELLED));
        when(bookingRepository.save(booking)).thenReturn(BOOKING_STUDIO_CANCELED);

        bookingService.cancelBooking(bookingId, userId);

        verify(bookingRepository).findByIdAndUserId(bookingId, userId);
        verify(bookingRepository).save(booking);
        verify(bookingStatusRepository).findByStatus(STATUS_CANCELLED);
        verifyNoMoreInteractions(bookingRepository, bookingStatusRepository);
    }

    @Test
    @DisplayName("Verify processExpiredBooking() method")
    void processExpiredBooking_NoExpiredBookingsTomorrow_ShouldReturnEmptyList() {
        LocalDate tomorrow = LocalDate.now().plusDays(1);

        when(bookingStatusRepository.findByStatus(STATUS_EXPIRED))
                .thenReturn(Optional.of(BOOKING_STATUS_EXPIRED));
        when(bookingRepository.findExpiringBookings(tomorrow)).thenReturn(List.of());

        bookingService.processExpiredBooking();
        verify(bookingStatusRepository).findByStatus(BookingStatus.Status.EXPIRED);
        verify(bookingRepository).findExpiringBookings(tomorrow);
        verifyNoMoreInteractions(bookingRepository, bookingStatusRepository);
    }

    @Test
    @DisplayName("Verify processExpiredBooking() method when expired bookings is present")
    void processExpiredBooking_HasExpiredBookingsTomorrow_ShouldReturnBookingList() {
        LocalDate tomorrow = LocalDate.now().plusDays(1);

        List<Booking> expiredBookings = List.of(BOOKING_CONDO_EXPIRED);

        when(bookingStatusRepository.findByStatus(STATUS_EXPIRED))
                .thenReturn(Optional.of(BOOKING_STATUS_EXPIRED));
        when(bookingRepository.findExpiringBookings(tomorrow)).thenReturn(expiredBookings);

        bookingService.processExpiredBooking();
        assertEquals(STATUS_EXPIRED, expiredBookings.get(0).getStatus().getStatus());
        verify(bookingStatusRepository).findByStatus(STATUS_EXPIRED);
        verify(bookingRepository).findExpiringBookings(tomorrow);
        verify(bookingRepository).save(BOOKING_CONDO_EXPIRED);
        verifyNoMoreInteractions(bookingRepository, bookingStatusRepository);
    }
}
