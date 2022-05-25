package com.ca.formation.service;

import com.ca.formation.model.Customer;

import java.util.Optional;

/**
 * @author tonux
 */
public interface CustomerService {

    Optional<Customer> getCustomer(Long id);

    Customer createCustomer(Customer customer);

    Customer updateCustomer(Customer customer);
}
