package de.oth.Packlon.service;


import de.oth.Packlon.entity.Address;
import de.oth.Packlon.repository.AddressRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AddressService {
    private final AddressRepository addressRepository;

    public AddressService(AddressRepository addressRepository) {
        this.addressRepository = addressRepository;
    }

    public Address createAddress(Address address) {
        Address ret = addressRepository.save(address);
        return ret;
    }

    public boolean existsAddress(Address address) {
        if (address.getAddition() != null)
            return addressRepository.existsAddressByAdditionAndCityAndCountryAndStreetAndPostCode(address.getAddition(), address.getCity(), address.getCountry(), address.getStreet(), address.getPostCode());
        return addressRepository.existsAddressByCityAndCountryAndStreetAndPostCode(address.getCity(), address.getCountry(), address.getStreet(), address.getPostCode());
    }

    public Address getAddress(Address address) {
        if (address.getAddition() != null)
            return addressRepository.getAddressByAdditionAndCityAndCountryAndStreetAndPostCode(address.getAddition(), address.getCity(), address.getCountry(), address.getStreet(), address.getPostCode());
        return addressRepository.getAddressByCityAndCountryAndStreetAndPostCode(address.getCity(), address.getCountry(), address.getStreet(), address.getPostCode());
    }
}
