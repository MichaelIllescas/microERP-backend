package com.imperialnet.customer_service.application.usecase;

import com.imperialnet.customer_service.application.dto.CustomerResponse;
import com.imperialnet.customer_service.application.port.out.CustomerRepositoryPort;
import com.imperialnet.customer_service.domain.model.Customer;
import com.imperialnet.customer_service.infrastructure.mapper.CustomerMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class GetCustomerServiceTest {

    private CustomerRepositoryPort repositoryPort;
    private CustomerMapper mapper;
    private GetCustomerService service;

    @BeforeEach
    void setUp() {
        repositoryPort = mock(CustomerRepositoryPort.class);
        mapper = mock(CustomerMapper.class);
        service = new GetCustomerService(repositoryPort, mapper);
    }

    @Test
    void shouldGetCustomerById() {
        Customer customer = Customer.builder().id(1L).firstName("Juan").build();
        CustomerResponse response = CustomerResponse.builder().id(1L).firstName("Juan").build();

        when(repositoryPort.findById(1L)).thenReturn(Optional.of(customer));
        when(mapper.toResponse(customer)).thenReturn(response);

        CustomerResponse result = service.getCustomerById(1L);

        assertEquals("Juan", result.getFirstName());
    }

    @Test
    void shouldThrowExceptionWhenCustomerNotFound() {
        when(repositoryPort.findById(99L)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> service.getCustomerById(99L));
    }

    @Test
    void shouldGetAllCustomers() {
        Customer c1 = Customer.builder().id(1L).firstName("Juan").build();
        Customer c2 = Customer.builder().id(2L).firstName("Ana").build();

        when(repositoryPort.findAll()).thenReturn(List.of(c1, c2));
        when(mapper.toResponseList(List.of(c1, c2)))
                .thenReturn(List.of(
                        CustomerResponse.builder().id(1L).firstName("Juan").build(),
                        CustomerResponse.builder().id(2L).firstName("Ana").build()
                ));

        List<CustomerResponse> result = service.getAllCustomers();

        assertEquals(2, result.size());
        verify(repositoryPort).findAll();
    }
}
