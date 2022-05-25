package com.ca.formation.service;

import com.ca.formation.model.Customer;
import com.ca.formation.repository.CustomerRepository;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.Optional;

/**
 * @author tonux
 */
@Service
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;

    public CustomerServiceImpl(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    public Optional<Customer> getCustomer(Long id) {
        return customerRepository.findById(id);
    }

    public Customer createCustomer(Customer customer) {

        Assert.notNull(customer, "Invalid customer");
        Assert.isNull(customer.getId(), "customer id should be null");
        Assert.notNull(customer.getName(), "Invalid customer name");

        return customerRepository.save(customer);
    }

    public Customer updateCustomer(Customer customer) {

        Assert.notNull(customer, "Invalid customer");
        Assert.notNull(customer.getId(), "Invalid customer id");

        return customerRepository.save(customer);
    }
}
