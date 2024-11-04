package bookingapp.repository.booking;

import bookingapp.exception.SpecificationProviderNotFoundException;
import bookingapp.model.booking.Booking;
import bookingapp.repository.SpecificationProvider;
import bookingapp.repository.SpecificationProviderManager;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class BookingSpecificationProviderManager implements SpecificationProviderManager<Booking> {
    private final List<SpecificationProvider<Booking>> specificationProviders;

    @Override
    public SpecificationProvider<Booking> getSpecificationProvider(String key) {
        return specificationProviders.stream()
                .filter(p -> p.getKey().equals(key))
                .findFirst()
                .orElseThrow(() -> new SpecificationProviderNotFoundException(
                        "Can't find correct specification provider for key " + key));
    }
}
