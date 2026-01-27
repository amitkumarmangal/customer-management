package com.business.util;

import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class UtilsTest {

    private Utils utils;

    @BeforeEach
    void setUp() {
        utils = new Utils();
    }

    @AfterEach
    void tearDown() {
        // VERY IMPORTANT to avoid test leakage
        RequestContextHolder.resetRequestAttributes();
    }

    @Test
    void shouldReturnAuthorizationHeader_whenRequestContextExists() {
        // Arrange
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getHeader("Authorization"))
                .thenReturn("Bearer test-token");

        ServletRequestAttributes attrs =
                new ServletRequestAttributes(request);

        RequestContextHolder.setRequestAttributes(attrs);

        // Act
        String authHeader = utils.getAuthHeader();

        // Assert
        assertEquals("Bearer test-token", authHeader);
    }

    @Test
    void shouldReturnNull_whenAuthorizationHeaderNotPresent() {
        // Arrange
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getHeader("Authorization")).thenReturn(null);

        ServletRequestAttributes attrs =
                new ServletRequestAttributes(request);

        RequestContextHolder.setRequestAttributes(attrs);

        // Act
        String authHeader = utils.getAuthHeader();

        // Assert
        assertNull(authHeader);
    }

    @Test
    void shouldReturnEmptyString_whenNoRequestContextExists() {
        // Arrange
        RequestContextHolder.resetRequestAttributes();

        // Act
        String authHeader = utils.getAuthHeader();

        // Assert
        assertEquals("", authHeader);
    }
}
