package com.business.services.impl;

import com.business.domain.Customer;
import com.business.domain.respositories.CustomerRepository;
import com.business.services.CustomerService;
import com.business.util.AccountUtil;
import com.business.util.Utils;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CustomerServiceImpl implements CustomerService {

    @PersistenceContext
    private EntityManager entityManager;

    private final CustomerRepository customerRepository;
    private final AccountUtil accountUtil;
    private final Utils utils;

    public CustomerServiceImpl(CustomerRepository customerRepository, AccountUtil accountUtil,Utils utils) {
        this.customerRepository = customerRepository;
        this.accountUtil = accountUtil;
        this.utils = utils;
    }

    @Override
    public List<Customer> getAllCustomers() {
        return customerRepository.findAll();
    }

    @Override
    public Optional<Customer> getCustomerById(Long customerId) {
        return customerRepository.findById(customerId);
    }

    @Transactional
    @Override
    public Customer addCustomer(Customer customer) {
        return customerRepository.save(customer);
    }

    @Transactional
    @Override
    public Customer updateCustomer(Customer customer, Long customerId) {
        Optional<Customer> optionalCustomer = customerRepository.findById(customerId);
        if (optionalCustomer.isPresent()) {
            Customer updatedCustomer=optionalCustomer.get();
            updatedCustomer.setFirstName(customer.getFirstName());
            updatedCustomer.setMiddleName(customer.getMiddleName());
            updatedCustomer.setLastName(customer.getLastName());
            updatedCustomer.setMobile(customer.getMobile());
            updatedCustomer.setDob(customer.getDob());
            updatedCustomer.setStatus(customer.getStatus());
            return customerRepository.save(updatedCustomer);
        }
        return null;
    }

    @Transactional
    @Override
    public Customer softDeleteCustomerById(Long customerId) {
        Optional<Customer> optionalCustomer = customerRepository.findById(customerId);
        if (optionalCustomer.isPresent()) {
            Customer removedCustomer = optionalCustomer.get();
            removedCustomer.setStatus("R");
            final String authToken=utils.getAuthHeader();
            removedCustomer.getCustomerAccounts().forEach(rec -> accountUtil.softDeleteAccounts(authToken, rec.getAccountId().getAccountId()));
            return customerRepository.save(removedCustomer);
        }
        return null;
    }

    @Transactional
    public boolean deleteCustomerById(Long customerId) {
        boolean flag = false;
        Optional<Customer> optionalCustomer = customerRepository.findById(customerId);
        if (optionalCustomer.isPresent()) {
            Customer removedCustomer = optionalCustomer.get();
            final String authToken=utils.getAuthHeader();
            removedCustomer.getCustomerAccounts().forEach(rec -> accountUtil.removeAccounts(authToken, rec.getAccountId().getAccountId()));
            entityManager.clear();
            optionalCustomer=customerRepository.findById(customerId);
            optionalCustomer.ifPresent(customerRepository::delete);
            flag=true;
        }
        return flag;
    }
}
