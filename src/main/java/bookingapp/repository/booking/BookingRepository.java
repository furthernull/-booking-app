package bookingapp.repository.booking;

import bookingapp.model.booking.Booking;
import bookingapp.model.booking.BookingStatus;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

public interface BookingRepository
        extends JpaRepository<Booking, Long>, JpaSpecificationExecutor<Booking> {
    @EntityGraph(attributePaths = {"accommodation", "user", "status"})
    List<Booking> findAllByUserId(Long userId, Pageable pageable);

    @EntityGraph(attributePaths = {"accommodation", "user", "status"})
    Optional<Booking> findByIdAndUserId(Long id, Long userId);

    @Query("SELECT b FROM Booking AS b WHERE b.accommodation.id = :accommodationId "
            + "AND b.status.status != :cancelledStatus "
            + "AND b.isDeleted = FALSE "
            + "AND (b.checkInDate < :endDate AND b.checkOutDate > :startDate)")
    List<Booking> findConflictingBooking(
            Long accommodationId,
            LocalDate startDate,
            LocalDate endDate,
            BookingStatus.Status cancelledStatus);
}
