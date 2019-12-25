package de.oth.Packlon.service;


import de.oth.Packlon.entity.Address;
import de.oth.Packlon.repository.AddressRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AddressService {
    @Autowired
    private AddressRepository addressRepository;

    public Address createAddress(Address address) {
        Address ret = addressRepository.save(address);
        return ret;

    }

    public boolean existsAddress(Address address) {
        return addressRepository.existsAddressByAdditionAndCityAndCountryAndStreetAndPostCode(address.getAddition(), address.getCity(), address.getCountry(), address.getStreet(), address.getPostCode());

    }
    public Address getAddress(Address address){
        return addressRepository.getAddressByAdditionAndCityAndCountryAndStreetAndPostCode(address.getAddition(), address.getCity(), address.getCountry(), address.getStreet(), address.getPostCode());
    }
}
