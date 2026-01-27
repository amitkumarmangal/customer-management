package com.business.services;

import com.business.domain.Customer;

import java.util.List;
import java.util.Optional;

public interface CustomerService {

    public List<Customer> getAllCustomers();

    public Optional<Customer> getCustomerById(Long customerId);

    public Customer addCustomer(Customer customer);

    public Customer updateCustomer(Customer customer,Long customerId);

    public boolean deleteCustomerById(Long customerId);

    public Customer softDeleteCustomerById(Long customerId);
}
