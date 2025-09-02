package com.imperialnet.customer_service.application.usecase;

import com.imperialnet.customer_service.application.dto.CreateCustomerRequest;
import com.imperialnet.customer_service.application.dto.CustomerResponse;
import com.imperialnet.customer_service.application.port.out.CustomerRepositoryPort;
import com.imperialnet.customer_service.domain.model.Customer;
import com.imperialnet.customer_service.infrastructure.mapper.CustomerMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CreateCustomerServiceTest {

    private CustomerRepositoryPort repositoryPort;
    private CustomerMapper mapper;
    private CreateCustomerService service;

    @BeforeEach
    void setUp() {
        repositoryPort = mock(CustomerRepositoryPort.class);
        mapper = mock(CustomerMapper.class);
        service = new CreateCustomerService(repositoryPort, mapper);
    }

    @Test
    void shouldCreateCustomerSuccessfully() {
        CreateCustomerRequest request = CreateCustomerRequest.builder()
                .firstName("Juan")
                .lastName("Pérez")
                .email("juan@test.com")
                .phone("123")
                .address("Calle 1")
                .build();

        Customer customer = Customer.builder().id(1L).email("juan@test.com").build();

        when(repositoryPort.existsByEmail("juan@test.com")).thenReturn(false);
        when(mapper.toDomain(request)).thenReturn(customer);
        when(repositoryPort.save(customer)).thenReturn(customer);
        when(mapper.toResponse(customer)).thenReturn(CustomerResponse.builder().id(1L).build());

        CustomerResponse response = service.createCustomer(request);

        assertNotNull(response);
        assertEquals(1L, response.getId());
        verify(repositoryPort).save(customer);
    }

    @Test
    void shouldThrowExceptionWhenEmailExists() {
        CreateCustomerRequest request = CreateCustomerRequest.builder()
                .email("juan@test.com")
                .build();

        when(repositoryPort.existsByEmail("juan@test.com")).thenReturn(true);

        assertThrows(IllegalArgumentException.class, () -> service.createCustomer(request));
    }
}
