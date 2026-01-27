package com.business.controller;

import com.business.domain.Customer;
import com.business.services.CustomerService;
import com.business.util.ErrorMessage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.sql.Date;
import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CustomerManagementControllerTest {

    @Mock
    private CustomerService customerService;

    @InjectMocks
    private CustomerManagementController controller;

    private Customer customer;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        customer = new Customer();
        customer.setCustomerId(1L);
        customer.setFirstName("John Doe");
        customer.setDob(Date.valueOf(LocalDate.now()));
        customer.setMobile("9999999999");
    }

    // getCustomerById tests
    @Test
    void testGetCustomerById_ValidId_Found() {
        when(customerService.getCustomerById(1L)).thenReturn(Optional.of(customer));

        ResponseEntity<?> response = controller.getCustomerById(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(customer, response.getBody());
    }

    @Test
    void testGetCustomerById_ValidId_NotFound() {
        when(customerService.getCustomerById(2L)).thenReturn(Optional.empty());

        ResponseEntity<?> response = controller.getCustomerById(2L);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertTrue(response.getBody() instanceof ErrorMessage);
    }

    @Test
    void testGetCustomerById_InvalidId_NullOrNegative() {
        ResponseEntity<?> response1 = controller.getCustomerById(null);
        ResponseEntity<?> response2 = controller.getCustomerById(-1L);

        assertEquals(HttpStatus.BAD_REQUEST, response1.getStatusCode());
        assertEquals(HttpStatus.BAD_REQUEST, response2.getStatusCode());
    }

    // getCustomers tests
    @Test
    void testGetCustomers_WithData() {
        when(customerService.getAllCustomers()).thenReturn(List.of(customer));

        ResponseEntity<?> response = controller.getCustomers();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(List.of(customer), response.getBody());
    }

    @Test
    void testGetCustomers_NoData() {
        when(customerService.getAllCustomers()).thenReturn(Collections.emptyList());

        ResponseEntity<?> response = controller.getCustomers();

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        assertTrue(response.getBody() instanceof ErrorMessage);
    }

    // addCustomer tests
    @Test
    void testAddCustomer_ValidCustomer() {
        when(customerService.addCustomer(customer)).thenReturn(customer);

        ResponseEntity<?> response = controller.addCustomer(customer);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(customer, response.getBody());
    }

    @Test
    void testAddCustomer_NullCustomer() {
        ResponseEntity<?> response = controller.addCustomer(null);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertTrue(response.getBody() instanceof ErrorMessage);
    }

    // updateCustomer tests
    @Test
    void testUpdateCustomer_ValidCustomerAndId_Found() {
        when(customerService.updateCustomer(customer, 1L)).thenReturn(customer);

        ResponseEntity<?> response = controller.updateCustomer(customer, 1L);

        assertEquals(HttpStatus.ACCEPTED, response.getStatusCode());
        assertEquals(customer, response.getBody());
    }

    @Test
    void testUpdateCustomer_ValidCustomerAndId_NotFound() {
        when(customerService.updateCustomer(customer, 2L)).thenReturn(null);

        ResponseEntity<?> response = controller.updateCustomer(customer, 2L);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertTrue(response.getBody() instanceof ErrorMessage);
    }

    @Test
    void testUpdateCustomer_NullCustomer() {
        ResponseEntity<?> response = controller.updateCustomer(null, 1L);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertTrue(response.getBody() instanceof ErrorMessage);
    }

    @Test
    void testUpdateCustomer_InvalidId() {
        ResponseEntity<?> response = controller.updateCustomer(customer, -1L);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertTrue(response.getBody() instanceof ErrorMessage);
    }

    // deleteCustomerById tests
    @Test
    void testDeleteCustomerById_Success() {
        when(customerService.deleteCustomerById(1L)).thenReturn(true);

        ResponseEntity<?> response = controller.deleteCustomerById(1L);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    void testDeleteCustomerById_NotFound() {
        when(customerService.deleteCustomerById(2L)).thenReturn(false);

        ResponseEntity<?> response = controller.deleteCustomerById(2L);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertTrue(response.getBody() instanceof ErrorMessage);
    }

    @Test
    void testDeleteCustomerById_InvalidId() {
        ResponseEntity<?> response = controller.deleteCustomerById(-1L);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertTrue(response.getBody() instanceof ErrorMessage);
    }

    // softDeleteCustomerById tests
    @Test
    void testSoftDeleteCustomerById_Success() {
        when(customerService.softDeleteCustomerById(1L)).thenReturn(customer);

        ResponseEntity<?> response = controller.softDeleteCustomerById(1L);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        assertEquals(customer, response.getBody());
    }

    @Test
    void testSoftDeleteCustomerById_NotFound() {
        when(customerService.softDeleteCustomerById(2L)).thenReturn(null);

        ResponseEntity<?> response = controller.softDeleteCustomerById(2L);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertTrue(response.getBody() instanceof ErrorMessage);
    }

    @Test
    void testSoftDeleteCustomerById_InvalidId() {
        ResponseEntity<?> response = controller.softDeleteCustomerById(-1L);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertTrue(response.getBody() instanceof ErrorMessage);
    }
}
