package de.oth.packlon.service;

import de.oth.packlon.entity.Customer;

public interface ICustomerService {
    Customer mergeCustomer(Customer customer);

    Customer getCustomerByName(Customer cust);

    Customer getCustomerById(long customerId);
}
