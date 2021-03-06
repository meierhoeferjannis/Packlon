package de.oth.packlon.repository;


import de.oth.packlon.entity.Address;
import org.springframework.data.repository.CrudRepository;

public interface AddressRepository extends CrudRepository<Address, Long> {
    Address getAddressByAdditionAndCityAndCountryAndStreetAndPostCode(String addition, String city, String country, String street, int postCode);
    boolean existsAddressByAdditionAndCityAndCountryAndStreetAndPostCode(String addition, String city, String country, String street, int postCode);
    Address getAddressByCityAndCountryAndStreetAndPostCode( String city, String country, String street, int postCode);
    boolean existsAddressByCityAndCountryAndStreetAndPostCode( String city, String country, String street, int postCode);
}
