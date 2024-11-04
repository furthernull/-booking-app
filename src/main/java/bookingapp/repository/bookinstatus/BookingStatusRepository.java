package bookingapp.repository.bookinstatus;

import bookingapp.model.booking.BookingStatus;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookingStatusRepository extends JpaRepository<BookingStatus, Long> {
    Optional<BookingStatus> findByStatus(BookingStatus.Status status);
}
