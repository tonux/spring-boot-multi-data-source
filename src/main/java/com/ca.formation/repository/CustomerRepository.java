package com.ca.formation.repository;

import com.ca.formation.model.Customer;
import com.ca.formation.repository.readRepository.CustomerReadRepository;
import com.ca.formation.repository.writeRepository.CustomerWriteRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CustomerRepository implements CustomerRepositoryCombo {

    private final CustomerReadRepository readRepository;
    private final CustomerWriteRepository writeRepository;

    public CustomerRepository(CustomerReadRepository customerReadRepository, CustomerWriteRepository customerWriteRepository) {
        this.readRepository = customerReadRepository;
        this.writeRepository = customerWriteRepository;
    }

    @Override
    public <S extends Customer> S save(S s) {
        return writeRepository.save(s);
    }

    @Override
    public <S extends Customer> Iterable<S> saveAll(Iterable<S> iterable) {
        return writeRepository.saveAll(iterable);
    }

    @Override
    public Optional<Customer> findById(Long aLong) {
        return readRepository.findById(aLong);
    }

    @Override
    public boolean existsById(Long aLong) {
        return readRepository.existsById(aLong);
    }

    @Override
    public Iterable<Customer> findAll() {
        return readRepository.findAll();
    }

    @Override
    public Iterable<Customer> findAllById(Iterable<Long> iterable) {
        return readRepository.findAllById(iterable);
    }

    @Override
    public long count() {
        return readRepository.count();
    }

    @Override
    public void deleteById(Long aLong) {
        writeRepository.deleteById(aLong);
    }

    @Override
    public void delete(Customer customer) {
        writeRepository.delete(customer);
    }

    @Override
    public void deleteAllById(Iterable<? extends Long> longs) {
        writeRepository.deleteAllById(longs);
    }

    @Override
    public void deleteAll(Iterable<? extends Customer> iterable) {
        writeRepository.deleteAll(iterable);
    }

    @Override
    public void deleteAll() {
        writeRepository.deleteAll();
    }
}
