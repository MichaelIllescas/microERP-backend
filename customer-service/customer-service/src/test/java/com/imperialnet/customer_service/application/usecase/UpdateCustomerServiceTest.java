package com.imperialnet.customer_service.application.usecase;

import com.imperialnet.customer_service.application.dto.CustomerResponse;
import com.imperialnet.customer_service.application.dto.UpdateCustomerRequest;
import com.imperialnet.customer_service.application.port.out.CustomerRepositoryPort;
import com.imperialnet.customer_service.domain.model.Customer;
import com.imperialnet.customer_service.infrastructure.mapper.CustomerMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UpdateCustomerServiceTest {

    private CustomerRepositoryPort repositoryPort;
    private CustomerMapper mapper;
    private UpdateCustomerService service;

    @BeforeEach
    void setUp() {
        repositoryPort = mock(CustomerRepositoryPort.class);
        mapper = mock(CustomerMapper.class);
        service = new UpdateCustomerService(repositoryPort, mapper);
    }

    @Test
    void shouldUpdateCustomerPartially() {
        Long id = 1L;
        Customer existing = Customer.builder()
                .id(id)
                .firstName("Juan")
                .lastName("Pérez")
                .email("juan@test.com")
                .status("ACTIVE")
                .build();

        UpdateCustomerRequest request = UpdateCustomerRequest.builder()
                .phone("999999")
                .build();

        Customer updated = existing.toBuilder().phone("999999").build();

        when(repositoryPort.findById(id)).thenReturn(Optional.of(existing));
        when(repositoryPort.save(existing)).thenReturn(updated);
        when(mapper.toResponse(updated)).thenReturn(CustomerResponse.builder().id(id).phone("999999").build());

        CustomerResponse response = service.updateCustomer(id, request);

        assertEquals("999999", response.getPhone());
        verify(repositoryPort).save(existing);
    }

    @Test
    void shouldThrowExceptionWhenCustomerNotFound() {
        when(repositoryPort.findById(99L)).thenReturn(Optional.empty());

        UpdateCustomerRequest request = UpdateCustomerRequest.builder().firstName("Nuevo").build();

        assertThrows(IllegalArgumentException.class, () -> service.updateCustomer(99L, request));
    }
}
