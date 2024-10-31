package bookingapp.service.impl;

import bookingapp.dto.accommodation.AccommodationDto;
import bookingapp.dto.accommodation.AccommodationRequestDto;
import bookingapp.exception.EntityNotFoundException;
import bookingapp.mapper.AccommodationMapper;
import bookingapp.model.accommodation.Accommodation;
import bookingapp.model.accommodation.Address;
import bookingapp.repository.accommodation.AccommodationRepository;
import bookingapp.service.AccommodationService;
import bookingapp.service.AddressService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class AccommodationServiceImpl implements AccommodationService {
    private final AccommodationRepository accommodationRepository;
    private final AccommodationMapper accommodationMapper;
    private final AddressService addressService;

    @Override
    public AccommodationDto create(AccommodationRequestDto requestDto) {
        Accommodation accommodation = accommodationMapper.toModel(requestDto);
        Address location = addressService.save(requestDto.address());
        accommodation.setLocation(location);
        return accommodationMapper.toDto(accommodationRepository.save(accommodation));
    }

    @Override
    public List<AccommodationDto> findAll(Pageable pageable) {
        return accommodationMapper.toDto(accommodationRepository.findAll(pageable));
    }

    @Override
    public AccommodationDto findById(Long id) {
        Accommodation accommodation = accommodationRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Accommodation not found, requested id: " + id)
        );
        return accommodationMapper.toDto(accommodation);
    }

    @Override
    public AccommodationDto update(Long id, AccommodationRequestDto requestDto) {
        Accommodation accommodation = accommodationRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Accommodation not found, requested id: " + id)
        );
        accommodationMapper.updateModel(accommodation, requestDto);
        return accommodationMapper.toDto(accommodationRepository.save(accommodation));
    }

    @Override
    public void deleteById(Long id) {
        accommodationRepository.deleteById(id);
    }
}
