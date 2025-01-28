package bookingapp.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import bookingapp.model.booking.Booking;
import bookingapp.model.booking.BookingStatus;
import bookingapp.repository.booking.BookingRepository;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.jdbc.Sql;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Sql(scripts = {"classpath:database/accommodation/delete-accommodation-related-data.sql",
        "classpath:database/user/delete-user-related-data.sql",
        "classpath:database/booking/delete-booking-related-data.sql",
        "classpath:database/accommodation/add-default-two-accommodation.sql",
        "classpath:database/user/add-default-users.sql",},
        executionPhase = Sql.ExecutionPhase.BEFORE_TEST_CLASS)
@Sql(scripts = {"classpath:database/booking/add-default-booking-status.sql",
        "classpath:database/booking/add-default-two-bookings.sql"},
        executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = {"classpath:database/booking/delete-booking-related-data.sql"},
        executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
class BookingRepositoryTest {
    @Autowired
    private BookingRepository bookingRepository;

    @Test
    @DisplayName("Verify findAllByUserId() method")
    void findAllByUserId_ValidUserId_ReturnsBookings() {
        Long userId = 1L;
        Pageable pageable = PageRequest.of(0, 10);

        List<Booking> bookings = bookingRepository.findAllByUserId(userId, pageable);
        assertEquals(2, bookings.size());
        assertEquals(userId, bookings.get(0).getUser().getId());
    }

    @Test
    @DisplayName("Verify findByIdAndUserId() method")
    void findByIdAndUserId_ValidIdAndUserId_ReturnsBookings() {
        Long bookingId = 1L;
        Long userId = 1L;

        Optional<Booking> bookings = bookingRepository.findByIdAndUserId(bookingId, userId);
        assertTrue(bookings.isPresent());
        assertEquals(bookingId, bookings.get().getId());
        assertEquals(userId, bookings.get().getUser().getId());
    }

    @Test
    @DisplayName("Verify findConflictingBooking() method")
    void findConflictingBooking_ValidBookedDates_ReturnFalse() {
        Long accommodationId = 1L;
        LocalDate startDate = LocalDate.now().plusDays(1);
        LocalDate endDate = startDate.plusMonths(1);
        BookingStatus.Status cancelled = BookingStatus.Status.CANCELLED;

        List<Booking> bookings = bookingRepository
                .findConflictingBooking(accommodationId, startDate, endDate, cancelled);
        assertFalse(bookings.isEmpty());
    }

    @Test
    @DisplayName("Verify findById() method")
    void findById_ValidId_ReturnsValidBooking() {
        Long bookingId = 1L;

        Optional<Booking> bookings = bookingRepository.findById(bookingId);
        assertTrue(bookings.isPresent());
        assertEquals(bookingId, bookings.get().getId());
    }

    @Test
    @DisplayName("Verify findExpiringBookings")
    void findExpiringBookings_ValidDate_ReturnEmptyList() {
        LocalDate today = LocalDate.now();

        List<Booking> bookings = bookingRepository.findExpiringBookings(today);
        assertTrue(bookings.isEmpty());
    }
}
