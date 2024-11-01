package bookingapp.repository.address;

import bookingapp.model.accommodation.Address;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AddressRepository extends JpaRepository<Address, Long> {
}
