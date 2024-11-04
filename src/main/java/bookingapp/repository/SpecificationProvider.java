package bookingapp.repository;

import bookingapp.model.booking.Booking;
import org.springframework.data.jpa.domain.Specification;

public interface SpecificationProvider<T> {
    String getKey();

    Specification<Booking> getSpecification(String param);
}
