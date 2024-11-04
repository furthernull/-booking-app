package bookingapp.repository;

import bookingapp.dto.booking.BookingFilterParameters;
import org.springframework.data.jpa.domain.Specification;

public interface SpecificationBuilder<T> {
    Specification<T> build(BookingFilterParameters filterParameters);
}
