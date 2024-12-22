package bookingapp.repository.paymentstatus;

import bookingapp.model.payment.PaymentStatus;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentStatusRepository extends JpaRepository<PaymentStatus, Long> {
    Optional<PaymentStatus> findByStatus(PaymentStatus.Status status);
}
