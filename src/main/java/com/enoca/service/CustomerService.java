package com.enoca.service;

import com.enoca.model.Customer;

import java.util.Optional;

public interface CustomerService {

    Customer addCustomer(Customer customer);

    Customer findById(Long id);
}
