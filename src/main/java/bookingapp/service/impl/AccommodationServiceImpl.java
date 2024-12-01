package bookingapp.service.impl;

import bookingapp.dto.accommodation.AccommodationDto;
import bookingapp.dto.accommodation.AccommodationRequestDto;
import bookingapp.exception.EntityNotFoundException;
import bookingapp.mapper.AccommodationMapper;
import bookingapp.model.accommodation.Accommodation;
import bookingapp.model.accommodation.AccommodationType;
import bookingapp.model.accommodation.Address;
import bookingapp.model.accommodation.AmenityType;
import bookingapp.repository.accommodation.AccommodationRepository;
import bookingapp.repository.accommodationtype.AccommodationTypeRepository;
import bookingapp.repository.amenity.AmenityRepository;
import bookingapp.service.AccommodationService;
import bookingapp.service.AddressService;
import bookingapp.service.NotificationService;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class AccommodationServiceImpl implements AccommodationService {
    private final AccommodationMapper accommodationMapper;
    private final AccommodationRepository accommodationRepository;
    private final AccommodationTypeRepository accommodationTypeRepository;
    private final AddressService addressService;
    private final AmenityRepository amenityRepository;
    private final NotificationService notificationService;

    @Transactional
    @Override
    public AccommodationDto create(AccommodationRequestDto requestDto) {
        Accommodation accommodation = accommodationMapper.toModel(requestDto);
        accommodation.setType(fetchAccommodationType(requestDto.accommodationTypeId()));
        Address location = addressService.save(requestDto.address());
        accommodation.setLocation(location);
        accommodation.setAmenities(fetchAmenitiesByIds(requestDto.amenityIds()));
        accommodationRepository.save(accommodation);
        notificationService.sendNotification(accommodation);
        return accommodationMapper.toDto(accommodation);
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

    private AccommodationType fetchAccommodationType(Long accommodationTypeId) {
        return accommodationTypeRepository.getReferenceById(accommodationTypeId);
    }

    private Set<AmenityType> fetchAmenitiesByIds(Set<Long> amenitiesIds) {
        return amenitiesIds.stream()
                .map(amenityRepository::getReferenceById)
                .collect(Collectors.toSet());
    }
}
