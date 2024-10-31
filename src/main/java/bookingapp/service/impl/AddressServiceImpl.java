package bookingapp.service.impl;

import bookingapp.dto.address.AddressRequestDto;
import bookingapp.mapper.AddressMapper;
import bookingapp.model.accommodation.Address;
import bookingapp.repository.address.AddressRepository;
import bookingapp.service.AddressService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class AddressServiceImpl implements AddressService {
    private final AddressRepository addressRepository;
    private final AddressMapper addressMapper;

    @Override
    public Address save(AddressRequestDto requestDto) {
        Address address = addressMapper.toModel(requestDto);
        return addressRepository.save(address);
    }
}
