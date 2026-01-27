package com.business.services.impl;

import com.business.domain.Customer;
import com.business.domain.Customer_X_Account;
import com.business.domain.Account;
import com.business.domain.respositories.CustomerRepository;
import com.business.util.AccountUtil;
import com.business.util.Utils;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.sql.Date;
import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CustomerServiceImplTest {

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private AccountUtil accountUtil;

    @Mock
    private Utils utils;

    @Mock
    private EntityManager entityManager;

    @InjectMocks
    private CustomerServiceImpl customerService;

    private Customer customer;

    @BeforeEach
    void setUp() {
        customer = new Customer();
        customer.setCustomerId(1L);
        customer.setFirstName("Amit");
        customer.setLastName("Mangal");
        customer.setMobile("9999999999");
        customer.setDob(Date.valueOf(LocalDate.now()));
        customer.setStatus("A");

        // 🔑 FIX: Inject EntityManager manually
        ReflectionTestUtils.setField(
                customerService,
                "entityManager",
                entityManager
        );
    }


    // -------------------------------------------------
    // getAllCustomers
    // -------------------------------------------------

    @Test
    void shouldReturnAllCustomers() {
        when(customerRepository.findAll()).thenReturn(List.of(customer));

        List<Customer> result = customerService.getAllCustomers();

        assertEquals(1, result.size());
        verify(customerRepository).findAll();
    }

    // -------------------------------------------------
    // getCustomerById
    // -------------------------------------------------

    @Test
    void shouldReturnCustomerWhenFound() {
        when(customerRepository.findById(1L))
                .thenReturn(Optional.of(customer));

        Optional<Customer> result =
                customerService.getCustomerById(1L);

        assertTrue(result.isPresent());
        assertEquals("Amit", result.get().getFirstName());
    }

    @Test
    void shouldReturnEmptyWhenCustomerNotFound() {
        when(customerRepository.findById(1L))
                .thenReturn(Optional.empty());

        Optional<Customer> result =
                customerService.getCustomerById(1L);

        assertFalse(result.isPresent());
    }

    // -------------------------------------------------
    // addCustomer
    // -------------------------------------------------

    @Test
    void shouldSaveCustomer() {
        when(customerRepository.save(customer))
                .thenReturn(customer);

        Customer saved = customerService.addCustomer(customer);

        assertNotNull(saved);
        verify(customerRepository).save(customer);
    }

    // -------------------------------------------------
    // updateCustomer
    // -------------------------------------------------

    @Test
    void shouldUpdateCustomerWhenExists() {
        Customer update = new Customer();
        update.setFirstName("Updated");
        update.setLastName("Name");
        update.setStatus("A");

        when(customerRepository.findById(1L))
                .thenReturn(Optional.of(customer));
        when(customerRepository.save(any(Customer.class)))
                .thenAnswer(inv -> inv.getArgument(0));

        Customer result =
                customerService.updateCustomer(update, 1L);

        assertNotNull(result);
        assertEquals("Updated", result.getFirstName());
        verify(customerRepository).save(customer);
    }

    @Test
    void shouldReturnNullWhenUpdatingNonExistingCustomer() {
        when(customerRepository.findById(1L))
                .thenReturn(Optional.empty());

        Customer result =
                customerService.updateCustomer(customer, 1L);

        assertNull(result);
        verify(customerRepository, never()).save(any());
    }

    // -------------------------------------------------
    // softDeleteCustomerById
    // -------------------------------------------------

    @Test
    void shouldSoftDeleteCustomerAndAccounts() {
        Customer_X_Account link = mock(Customer_X_Account.class);
        Account account = mock(Account.class);

        when(account.getAccountId()).thenReturn(100L);
        when(link.getAccountId()).thenReturn(account);

        customer.setCustomerAccounts(Set.of(link));

        when(customerRepository.findById(1L))
                .thenReturn(Optional.of(customer));
        when(utils.getAuthHeader())
                .thenReturn("Bearer token");
        when(customerRepository.save(customer))
                .thenReturn(customer);

        Customer result =
                customerService.softDeleteCustomerById(1L);

        assertNotNull(result);
        assertEquals("R", result.getStatus());

        verify(accountUtil)
                .softDeleteAccounts("Bearer token", 100L);
        verify(customerRepository).save(customer);
    }

    @Test
    void shouldReturnNullWhenSoftDeleteCustomerNotFound() {
        when(customerRepository.findById(1L))
                .thenReturn(Optional.empty());

        Customer result =
                customerService.softDeleteCustomerById(1L);

        assertNull(result);
        verify(accountUtil, never()).softDeleteAccounts(any(), anyLong());
    }

    // -------------------------------------------------
    // deleteCustomerById
    // -------------------------------------------------

    @Test
    void shouldDeleteCustomerAndAccounts() {
        Customer_X_Account link = mock(Customer_X_Account.class);
        Account account = mock(Account.class);

        when(account.getAccountId()).thenReturn(200L);
        when(link.getAccountId()).thenReturn(account);

        customer.setCustomerAccounts(Set.of(link));

        when(customerRepository.findById(1L))
                .thenReturn(Optional.of(customer))
                .thenReturn(Optional.of(customer));

        when(utils.getAuthHeader()).thenReturn("Bearer token");

        boolean result =
                customerService.deleteCustomerById(1L);

        assertTrue(result);
        verify(accountUtil)
                .removeAccounts("Bearer token", 200L);
        verify(entityManager).clear();
        verify(customerRepository).delete(customer);
    }

    @Test
    void shouldReturnFalseWhenDeletingNonExistingCustomer() {
        when(customerRepository.findById(1L))
                .thenReturn(Optional.empty());

        boolean result =
                customerService.deleteCustomerById(1L);

        assertFalse(result);
        verify(customerRepository, never()).delete(any());
    }
}
