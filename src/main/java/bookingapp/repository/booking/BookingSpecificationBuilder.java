package bookingapp.repository.booking;

import bookingapp.dto.booking.BookingFilterParameters;
import bookingapp.model.booking.Booking;
import bookingapp.repository.SpecificationBuilder;
import bookingapp.repository.SpecificationProviderManager;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class BookingSpecificationBuilder implements SpecificationBuilder<Booking> {
    private final SpecificationProviderManager<Booking> specificationProviderManager;

    @Override
    public Specification<Booking> build(BookingFilterParameters filterParameters) {
        Specification<Booking> spec = Specification.where(null);
        if (filterParameters.status() != null) {
            spec = spec.and(specificationProviderManager.getSpecificationProvider("status")
                    .getSpecification(filterParameters.status().toString()));
        }
        if (filterParameters.userId() != null) {
            spec = spec.and(specificationProviderManager.getSpecificationProvider("user")
                    .getSpecification(filterParameters.userId().toString()));
        }
        return spec;
    }
}
