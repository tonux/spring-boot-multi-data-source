package com.ca.formation.repository.writeRepository;

import com.ca.formation.model.Customer;
import org.springframework.data.repository.CrudRepository;

/**
 * @author tonux
 */
public interface CustomerWriteRepository extends CrudRepository<Customer, Long> {
}
