package de.oth.Packlon.service;

import de.oth.Packlon.entity.Customer;
import de.oth.Packlon.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CustomerService {
    @Autowired
    private CustomerRepository customerRepository;

    public Customer mergeCustomer(Customer customer) {
        Customer ret = customerRepository.save(customer);
        return ret;

    }

    public Customer getCustomerByName(Customer cust) {
        Optional<Customer> customer = customerRepository.findByFirstNameAndLastName(cust.getFirstName(), cust.getLastName());

        if (customer.isPresent()){
            return customer.get();
        }
        else {
            Customer newCustomer = new Customer();
            newCustomer.setFirstName(cust.getFirstName());
            newCustomer.setLastName(cust.getLastName());
            return mergeCustomer(newCustomer);
        }


    }

    public Customer getCustomerById(long customerId) {
        Optional<Customer> customer = customerRepository.findById(customerId);
        return customer.get();
    }

}
