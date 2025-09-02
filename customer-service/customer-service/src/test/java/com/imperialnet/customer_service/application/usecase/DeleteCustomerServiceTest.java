package com.imperialnet.customer_service.application.usecase;

import com.imperialnet.customer_service.application.port.out.CustomerRepositoryPort;
import com.imperialnet.customer_service.domain.model.Customer;
import com.imperialnet.customer_service.infrastructure.mapper.CustomerMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class DeleteCustomerServiceTest {

    private CustomerRepositoryPort repositoryPort;
    private DeleteCustomerService service;

    @BeforeEach
    void setUp() {
        repositoryPort = mock(CustomerRepositoryPort.class);
        service = new DeleteCustomerService(repositoryPort);
    }

    @Test
    void shouldDeleteCustomerSuccessfully() {
        when(repositoryPort.findById(1L)).thenReturn(Optional.of(Customer.builder().id(1L).build()));

        service.deleteCustomer(1L);

        verify(repositoryPort).deleteById(1L);
    }

    @Test
    void shouldThrowExceptionWhenCustomerNotFound() {
        when(repositoryPort.findById(99L)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> service.deleteCustomer(99L));
    }
}
