package bookingapp.repository.accommodationtype;

import bookingapp.model.accommodation.AccommodationType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccommodationTypeRepository extends JpaRepository<AccommodationType, Long> {
}
