package com.ca.formation.controller;

import com.ca.formation.handler.ResourceNotFoundException;
import com.ca.formation.model.Customer;
import com.ca.formation.service.CustomerService;
import org.springframework.web.bind.annotation.*;

/**
 * @author tonux
 */
@RestController
public class CustomerControllerImpl {

    private final CustomerService customerService;

    public CustomerControllerImpl(CustomerService customerService) {
        this.customerService = customerService;
    }

    @RequestMapping(path = "/customer/{id}", method = RequestMethod.GET)
    public Customer getCustomer(@PathVariable("id") Long id) {

        return customerService.getCustomer(id).orElseThrow(() -> new ResourceNotFoundException("Invalid Customer"));
    }

    @RequestMapping(path = "/customer", method = RequestMethod.POST)
    public Customer createCustomer(@RequestBody Customer customer) {
        return customerService.createCustomer(customer);
    }

    @RequestMapping(path = "/customer", method = RequestMethod.PUT)
    public Customer updateCustomer(@RequestBody Customer customer) {
        return customerService.updateCustomer(customer);
    }
}
