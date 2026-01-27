package com.business.controller;

import com.business.domain.Customer;
import com.business.services.CustomerService;
import com.business.util.ErrorMessage;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/rest/api/customers/v1")
public class CustomerManagementController {

    private final CustomerService customerService;

    public CustomerManagementController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getCustomerById(@PathVariable("id") Long customerId) {
        if (customerId != null && customerId > 0) {
            Optional<Customer> optionalCustomer = customerService.getCustomerById(customerId);
            if (optionalCustomer.isPresent()) {
                return ResponseEntity.status(HttpStatus.OK).body(optionalCustomer.get());
            }
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorMessage(HttpStatus.NOT_FOUND.value(), "Customer ID: " + customerId + " not Exist."));
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorMessage(HttpStatus.BAD_REQUEST.value(), "Valid Customer ID  not Found"));
    }

    @GetMapping
    public ResponseEntity<?> getCustomers() {
        List<Customer> customers = customerService.getAllCustomers();
        if (!customers.isEmpty()) {
            return ResponseEntity.status(HttpStatus.OK).body(customers);
        }
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(new ErrorMessage(HttpStatus.NO_CONTENT.value(), "No Customer Exist."));
    }

    @PostMapping
    public ResponseEntity<?> addCustomer(@Valid @RequestBody Customer customer) {
        if (customer != null) {
            Customer savedCustomer = customerService.addCustomer(customer);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedCustomer);
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorMessage(HttpStatus.BAD_REQUEST.value(), "Customer Data Not Exist."));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateCustomer(@Valid @RequestBody Customer customer, @PathVariable("id") Long customerId) {
        if (customer != null) {
            if (customerId != null && customerId > 0) {
                Customer updatedCustomer = customerService.updateCustomer(customer, customerId);
                if (updatedCustomer != null) {
                    return ResponseEntity.status(HttpStatus.ACCEPTED).body(updatedCustomer);
                }
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorMessage(HttpStatus.NOT_FOUND.value(), "Customer ID: " + customerId + " not Exist."));
            }
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorMessage(HttpStatus.BAD_REQUEST.value(), "Valid Customer ID not Found"));
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorMessage(HttpStatus.BAD_REQUEST.value(), "Customer Data Not Exist."));
    }

    @DeleteMapping("/remove/{id}")
    public ResponseEntity<?> deleteCustomerById(@PathVariable("id") Long customerId) {
        if (customerId != null && customerId > 0) {
            if (customerService.deleteCustomerById(customerId)) {
                return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
            }
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorMessage(HttpStatus.NOT_FOUND.value(), "Customer ID: " + customerId + " not Exist."));
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorMessage(HttpStatus.BAD_REQUEST.value(), "Valid Customer ID not Found"));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> softDeleteCustomerById(@PathVariable("id") Long customerId) {
        if (customerId != null && customerId > 0) {
            Customer customer = customerService.softDeleteCustomerById(customerId);
            if (customer != null) {
                return ResponseEntity.status(HttpStatus.NO_CONTENT).body(customer);
            }
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorMessage(HttpStatus.NOT_FOUND.value(), "Customer ID: " + customerId + " not Exist."));
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorMessage(HttpStatus.BAD_REQUEST.value(), "Valid Customer ID not Found"));
    }
}
