package bookingapp.repository.amenity;

import bookingapp.model.accommodation.AmenityType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AmenityRepository extends JpaRepository<AmenityType, Long> {
}
