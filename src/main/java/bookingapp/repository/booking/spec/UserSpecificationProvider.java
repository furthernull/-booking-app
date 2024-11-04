package bookingapp.repository.booking.spec;

import bookingapp.model.booking.Booking;
import bookingapp.repository.SpecificationProvider;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@Component
public class UserSpecificationProvider implements SpecificationProvider<Booking> {
    @Override
    public String getKey() {
        return "user";
    }

    @Override
    public Specification<Booking> getSpecification(String param) {
        long userId = Long.parseLong(param);
        return (root, query, criteriaBuilder) -> criteriaBuilder
                .equal(root.get("user").get("id"), userId);
    }
}
