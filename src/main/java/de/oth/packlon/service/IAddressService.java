package de.oth.packlon.service;

import de.oth.packlon.entity.Address;

public interface IAddressService {
    Address createAddress(Address address);

    boolean existsAddress(Address address);

    Address getAddress(Address address);
}
