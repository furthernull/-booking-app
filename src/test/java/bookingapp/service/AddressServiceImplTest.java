package bookingapp.service;

import static bookingapp.test.TestUtils.ADDRESS;
import static bookingapp.test.TestUtils.ADDRESS_REQUEST_DTO;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import bookingapp.mapper.AddressMapper;
import bookingapp.model.accommodation.Address;
import bookingapp.repository.address.AddressRepository;
import bookingapp.service.impl.AddressServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class AddressServiceImplTest {
    @Mock
    private AddressRepository addressRepository;
    @Mock
    private AddressMapper addressMapper;
    @InjectMocks
    private AddressServiceImpl addressService;

    @Test
    @DisplayName("Verify save() method")
    void save_ValidAddressRequestDto_ReturnsValidAddressDto() {
        Address expected = ADDRESS;

        when(addressMapper.toModel(ADDRESS_REQUEST_DTO)).thenReturn(expected);
        when(addressRepository.save(expected)).thenReturn(expected);

        Address actual = addressService.save(ADDRESS_REQUEST_DTO);
        assertNotNull(actual);
        assertEquals(expected, actual);
        verify(addressRepository, times(1)).save(expected);
        verify(addressMapper, times(1)).toModel(ADDRESS_REQUEST_DTO);
        verifyNoMoreInteractions(addressMapper, addressRepository);

    }
}
