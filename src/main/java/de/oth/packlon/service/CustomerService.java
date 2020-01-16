package de.oth.packlon.service;

import de.oth.packlon.entity.Customer;
import de.oth.packlon.repository.CustomerRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CustomerService implements ICustomerService {
    private final CustomerRepository customerRepository;

    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @Override
    public Customer mergeCustomer(Customer customer) {
        Customer ret = customerRepository.save(customer);
        return ret;

    }

    @Override
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

    @Override
    public Customer getCustomerById(long customerId) {
        Optional<Customer> customer = customerRepository.findById(customerId);
        return customer.get();
    }

}
