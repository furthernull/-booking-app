package bookingapp.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import bookingapp.model.booking.BookingStatus;
import bookingapp.repository.bookingstatus.BookingStatusRepository;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Sql(scripts = "classpath:database/booking/delete-booking-related-data.sql",
        executionPhase = Sql.ExecutionPhase.BEFORE_TEST_CLASS)
class BookingStatusRepositoryTest {
    @Autowired
    private BookingStatusRepository bookingStatusRepository;

    @Test
    @DisplayName("Verify findByStatus() method")
    @Sql(scripts = "classpath:database/booking/add-default-booking-status.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "classpath:database/booking/delete-booking-related-data.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void findByStatus_ValidStatus_ReturnValidStatus() {
        BookingStatus.Status status = BookingStatus.Status.PENDING;

        Optional<BookingStatus> byStatus = bookingStatusRepository.findByStatus(status);
        assertTrue(byStatus.isPresent());
        assertEquals(status, byStatus.get().getStatus());
    }
}
