package bookingapp.service;

import static bookingapp.test.TestUtils.ACCOMMODATION_DTO_STUDIO;
import static bookingapp.test.TestUtils.ACCOMMODATION_DTO_UPDATED_TO_HOUSE;
import static bookingapp.test.TestUtils.ACCOMMODATION_PAGE;
import static bookingapp.test.TestUtils.ACCOMMODATION_REQUEST_DTO_STUDIO;
import static bookingapp.test.TestUtils.ACCOMMODATION_STUDIO;
import static bookingapp.test.TestUtils.ACCOMMODATION_UPDATED_TO_HOUSE;
import static bookingapp.test.TestUtils.ACCOMMODATION_UPDATE_REQUEST_DTO_HOUSE;
import static bookingapp.test.TestUtils.DEFAULT_ID_ONE;
import static bookingapp.test.TestUtils.PAGEABLE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import bookingapp.dto.accommodation.AccommodationDto;
import bookingapp.exception.EntityNotFoundException;
import bookingapp.mapper.AccommodationMapper;
import bookingapp.model.accommodation.Accommodation;
import bookingapp.repository.accommodation.AccommodationRepository;
import bookingapp.repository.accommodationtype.AccommodationTypeRepository;
import bookingapp.repository.amenity.AmenityRepository;
import bookingapp.service.impl.AccommodationServiceImpl;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class AccommodationServiceImplTest {
    @Mock
    private AccommodationMapper accommodationMapper;
    @Mock
    private AccommodationRepository accommodationRepository;
    @Mock
    private AccommodationTypeRepository accommodationTypeRepository;
    @Mock
    private AddressService addressService;
    @Mock
    private AmenityRepository amenityRepository;
    @Mock
    private NotificationService notificationService;
    @InjectMocks
    private AccommodationServiceImpl accommodationService;

    @Test
    @DisplayName("Verify create() method with valid request")
    void create_ValidRequestDto_ReturnNewAccommodationDto() {
        Accommodation accommodation = ACCOMMODATION_STUDIO;
        AccommodationDto expected = ACCOMMODATION_DTO_STUDIO;

        when(accommodationMapper.toModel(ACCOMMODATION_REQUEST_DTO_STUDIO))
                .thenReturn(accommodation);
        when(accommodationRepository.save(accommodation)).thenReturn(accommodation);
        when(accommodationMapper.toDto(accommodation)).thenReturn(expected);

        AccommodationDto actual = accommodationService.create(ACCOMMODATION_REQUEST_DTO_STUDIO);
        assertNotNull(actual);
        assertEquals(expected, actual);

        verify(accommodationMapper, times(1)).toModel(ACCOMMODATION_REQUEST_DTO_STUDIO);
        verify(accommodationRepository, times(1)).save(accommodation);
        verify(accommodationMapper, times(1)).toDto(accommodation);
        verifyNoMoreInteractions(accommodationMapper, accommodationRepository);
    }

    @Test
    @DisplayName("Verify findAll() method")
    void findAll_ReturnValidListOfAccommodationDto() {
        AccommodationDto expected = ACCOMMODATION_DTO_STUDIO;

        when(accommodationRepository.findAll(PAGEABLE)).thenReturn(ACCOMMODATION_PAGE);
        when(accommodationMapper.toDto(ACCOMMODATION_PAGE)).thenReturn(List.of(expected));

        List<AccommodationDto> actual = accommodationService.findAll(PAGEABLE);
        assertNotNull(actual);
        assertFalse(actual.isEmpty());
        assertEquals(expected, actual.get(0));

        verify(accommodationMapper, times(1)).toDto(ACCOMMODATION_PAGE);
        verify(accommodationRepository, times(1)).findAll(PAGEABLE);
        verifyNoMoreInteractions(accommodationMapper, accommodationRepository);
    }

    @Test
    @DisplayName("Verify findById() method with valid id")
    void findById_ValidId_ReturnValidAccommodationDto() {
        Accommodation accommodation = ACCOMMODATION_STUDIO;
        AccommodationDto expected = ACCOMMODATION_DTO_STUDIO;

        when(accommodationRepository.findById(DEFAULT_ID_ONE))
                .thenReturn(Optional.of(accommodation));
        when(accommodationMapper.toDto(accommodation)).thenReturn(expected);

        AccommodationDto actualAccommodationDto = accommodationService.findById(DEFAULT_ID_ONE);
        assertNotNull(actualAccommodationDto);
        assertEquals(expected, actualAccommodationDto);
        verify(accommodationMapper, times(1)).toDto(accommodation);
        verify(accommodationRepository, times(1)).findById(DEFAULT_ID_ONE);
        verifyNoMoreInteractions(accommodationMapper, accommodationRepository);
    }

    @Test
    @DisplayName("Verify findById() method with not valid id")
    void findById_NotValidId_ThrowEntityNotFoundException() {
        Long id = -1L;
        String expected = "Accommodation not found, requested id: " + id;

        when(accommodationRepository.findById(anyLong())).thenReturn(Optional.empty());

        EntityNotFoundException ex = assertThrows(EntityNotFoundException.class,
                () -> accommodationService.findById(id));
        assertEquals(expected, ex.getMessage());
    }

    @Test
    @DisplayName("Verify update() method with valid id and request")
    void update_ValidIdAndRequestDto_ReturnUpdatedAccommodationDto() {
        Accommodation accommodation = ACCOMMODATION_STUDIO;
        Accommodation updatedAccommodation = ACCOMMODATION_UPDATED_TO_HOUSE;
        AccommodationDto expected = ACCOMMODATION_DTO_UPDATED_TO_HOUSE;

        when(accommodationRepository.findById(anyLong())).thenReturn(Optional.of(accommodation));
        when(accommodationRepository.save(accommodation)).thenReturn(updatedAccommodation);
        when(accommodationMapper.toDto(updatedAccommodation)).thenReturn(expected);

        AccommodationDto actual = accommodationService
                .update(DEFAULT_ID_ONE, ACCOMMODATION_UPDATE_REQUEST_DTO_HOUSE);
        assertNotNull(actual);
        assertEquals(expected, actual);
        verify(accommodationRepository, times(1)).findById(DEFAULT_ID_ONE);
        verify(accommodationMapper, times(1))
                .updateModel(accommodation, ACCOMMODATION_UPDATE_REQUEST_DTO_HOUSE);
        verify(accommodationRepository, times(1)).save(accommodation);
        verify(accommodationMapper, times(1)).toDto(updatedAccommodation);
        verifyNoMoreInteractions(accommodationMapper, accommodationRepository);
    }

    @Test
    @DisplayName("Verify update() method with not valid params")
    void update_NotValidId_ThrowEntityNotFoundException() {
        Long id = -1L;
        String expected = "Accommodation not found, requested id: " + id;

        when(accommodationRepository.findById(anyLong())).thenReturn(Optional.empty());

        EntityNotFoundException ex = assertThrows(EntityNotFoundException.class,
                () -> accommodationService.update(id, ACCOMMODATION_UPDATE_REQUEST_DTO_HOUSE));
        assertEquals(expected, ex.getMessage());
    }

    @Test
    @DisplayName("Verify deleteById() method")
    void deleteById_ValidId_DeleteAccommodation() {
        accommodationService.deleteById(anyLong());
        verify(accommodationRepository, times(1)).deleteById(anyLong());
        verifyNoMoreInteractions(accommodationRepository);
    }
}
