package bookingapp.service;

import bookingapp.dto.address.AddressRequestDto;
import bookingapp.model.accommodation.Address;

public interface AddressService {
    Address save(AddressRequestDto requestDto);
}
