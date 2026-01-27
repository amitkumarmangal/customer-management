package com.business.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class AspectLoggerTest {

    @InjectMocks
    private LoggingAspect loggingAspect;


    @Mock
    private ProceedingJoinPoint joinPoint;

    @Mock
    private MethodSignature methodSignature;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldLogAndReturnResult_whenMethodSucceeds() throws Throwable {
        // Arrange
        when(joinPoint.getSignature()).thenReturn(methodSignature);
        when(methodSignature.getDeclaringType()).thenReturn(TestService.class);
        when(methodSignature.getName()).thenReturn("testMethod");
        when(joinPoint.getArgs()).thenReturn(new Object[]{"arg1", 10});
        when(joinPoint.proceed()).thenReturn("SUCCESS");

        // Act
        Object result = loggingAspect.logController(joinPoint);

        // Assert
        assertEquals("SUCCESS", result);
        verify(joinPoint, times(1)).proceed();
    }

    @Test
    void shouldLogAndRethrowException_whenMethodThrowsException() throws Throwable {
        // Arrange
        when(joinPoint.getSignature()).thenReturn(methodSignature);
        when(methodSignature.getDeclaringType()).thenReturn(TestService.class);
        when(methodSignature.getName()).thenReturn("testMethod");
        when(joinPoint.getArgs()).thenReturn(new Object[]{"arg1"});
        when(joinPoint.proceed()).thenThrow(new RuntimeException("RunTimeExcpetion"));

        // Act & Assert
        RuntimeException ex = assertThrows(
                RuntimeException.class,
                () -> loggingAspect.logController(joinPoint)
        );

        assertEquals("RunTimeExcpetion", ex.getMessage());
        verify(joinPoint, times(1)).proceed();
    }

    // Dummy class for signature metadata
    static class TestService {
        public String testMethod(String arg, int value) {
            return "OK";
        }
    }
}
