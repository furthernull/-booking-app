package bookingapp.repository.payment;

import bookingapp.model.payment.Payment;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
    Page<Payment> findAll(Pageable pageable);

    @Query("SELECT p FROM Payment p "
            + "JOIN FETCH p.status "
            + "JOIN FETCH p.booking b "
            + "JOIN FETCH b.user u "
            + "WHERE u.id = :userId")
    List<Payment> findByBookingUserId(Long userId, Pageable pageable);

    @Query("SELECT p FROM Payment p "
            + "JOIN FETCH p.booking b "
            + "JOIN FETCH b.user "
            + "WHERE p.sessionId = :sessionId")
    Optional<Payment> findBySessionId(String sessionId);
}
