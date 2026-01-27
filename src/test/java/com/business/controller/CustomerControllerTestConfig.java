package com.business.controller;

import com.business.services.CustomerService;
import org.mockito.Mockito;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

@TestConfiguration
public class CustomerControllerTestConfig {

    @Bean
    public CustomerService customerService() {
        return Mockito.mock(CustomerService.class);
    }
}
