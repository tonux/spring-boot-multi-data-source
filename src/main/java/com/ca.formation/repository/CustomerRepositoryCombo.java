package com.ca.formation.repository;

import com.ca.formation.repository.readRepository.CustomerReadRepository;
import com.ca.formation.repository.writeRepository.CustomerWriteRepository;

public interface CustomerRepositoryCombo extends CustomerReadRepository, CustomerWriteRepository {

}
