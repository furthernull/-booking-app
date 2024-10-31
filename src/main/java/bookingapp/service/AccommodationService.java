package bookingapp.service;

import bookingapp.dto.accommodation.AccommodationDto;
import bookingapp.dto.accommodation.AccommodationRequestDto;
import java.util.List;
import org.springframework.data.domain.Pageable;

public interface AccommodationService {
    AccommodationDto create(AccommodationRequestDto requestDto);

    List<AccommodationDto> findAll(Pageable pageable);

    AccommodationDto findById(Long id);

    AccommodationDto update(Long id, AccommodationRequestDto requestDto);

    void deleteById(Long id);
}
