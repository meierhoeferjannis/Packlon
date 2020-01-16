package de.oth.packlon.service;


import de.oth.packlon.entity.Address;
import de.oth.packlon.repository.AddressRepository;
import org.springframework.stereotype.Service;

@Service
public class AddressService implements IAddressService {
    private final AddressRepository addressRepository;

    public AddressService(AddressRepository addressRepository) {
        this.addressRepository = addressRepository;
    }

    @Override
    public Address createAddress(Address address) {
        Address ret = addressRepository.save(address);
        return ret;
    }

    @Override
    public boolean existsAddress(Address address) {
        if (address.getAddition() != null)
            return addressRepository.existsAddressByAdditionAndCityAndCountryAndStreetAndPostCode(address.getAddition(), address.getCity(), address.getCountry(), address.getStreet(), address.getPostCode());
        return addressRepository.existsAddressByCityAndCountryAndStreetAndPostCode(address.getCity(), address.getCountry(), address.getStreet(), address.getPostCode());
    }

    @Override
    public Address getAddress(Address address) {
        if (address.getAddition() != null)
            return addressRepository.getAddressByAdditionAndCityAndCountryAndStreetAndPostCode(address.getAddition(), address.getCity(), address.getCountry(), address.getStreet(), address.getPostCode());
        return addressRepository.getAddressByCityAndCountryAndStreetAndPostCode(address.getCity(), address.getCountry(), address.getStreet(), address.getPostCode());
    }
}
