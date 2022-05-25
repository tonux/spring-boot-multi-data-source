package com.ca.formation.repository.readRepository;

import com.ca.formation.model.Customer;
import org.springframework.data.repository.CrudRepository;

/**
 * @author tonux
 */
public interface CustomerReadRepository extends CrudRepository<Customer, Long> {
}
