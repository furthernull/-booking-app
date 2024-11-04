package bookingapp.repository.booking.spec;

import bookingapp.model.booking.Booking;
import bookingapp.model.booking.BookingStatus;
import bookingapp.repository.SpecificationProvider;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@Component
public class StatusSpecificationProvider implements SpecificationProvider<Booking> {

    @Override
    public String getKey() {
        return "status";
    }

    @Override
    public Specification<Booking> getSpecification(String param) {
        BookingStatus.Status status = BookingStatus.Status.valueOf(param);
        return (root, query, criteriaBuilder) -> criteriaBuilder
                .equal(root.get("status").get("status"), status);
    }
}
