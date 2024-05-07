package com.enoca.service.impl;

import com.enoca.model.Customer;
import com.enoca.repository.CustomerRepository;
import com.enoca.service.CustomerService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;

    public CustomerServiceImpl(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @Override
    public Customer addCustomer(Customer customer) {
        Customer newCustomer = new Customer(); //Yeni customer oluşturur

        newCustomer.setUsername(customer.getUsername());
        newCustomer.setCreatedAt(LocalDateTime.now());
        newCustomer.setUpdatedAt(LocalDateTime.now());

        return customerRepository.save(newCustomer);
    }

    @Override
    public Customer findById(Long id) {
        return customerRepository.findById(id).orElseThrow();
    }
}
