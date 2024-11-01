package bookingapp.dto.accommodation;

import java.math.BigDecimal;
import java.util.Set;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AccommodationDto {
    private Long id;
    private String type;
    private String location;
    private String size;
    private Set<Long> amenityIds;
    private BigDecimal dailyRate;
    private Integer availability;
}
