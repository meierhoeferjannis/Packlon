package de.oth.Packlon.repository;


import de.oth.Packlon.entity.Address;
import org.springframework.data.repository.CrudRepository;

public interface AddressRepository extends CrudRepository<Address, Long> {
    Address getAddressByAdditionAndCityAndCountryAndStreetAndPostCode(String addition, String city, String country, String street, int postCode);
    boolean existsAddressByAdditionAndCityAndCountryAndStreetAndPostCode(String addition, String city, String country, String street, int postCode);
}
