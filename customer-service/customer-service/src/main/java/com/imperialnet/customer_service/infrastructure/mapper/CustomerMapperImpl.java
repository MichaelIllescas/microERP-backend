package com.imperialnet.customer_service.infrastructure.mapper;

import com.imperialnet.customer_service.application.dto.*;
import com.imperialnet.customer_service.domain.model.Customer;
import com.imperialnet.customer_service.infrastructure.persistence.entity.CustomerEntity;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Implementación manual del mapper de clientes.
 */
@Component
public class CustomerMapperImpl implements CustomerMapper {

    // ================= DTO -> Dominio =================

    @Override
    public Customer toDomain(CreateCustomerRequest request) {
        if (request == null) return null;

        return Customer.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .phone(request.getPhone())
                .address(request.getAddress())
                .status("ACTIVE") // default al crear
                .build();
    }

    @Override
    public Customer toDomain(UpdateCustomerRequest request) {
        if (request == null) return null;

        return Customer.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .phone(request.getPhone())
                .address(request.getAddress())
                .status(request.getStatus())
                .build();
    }

    // ================= Dominio -> DTO =================

    @Override
    public CustomerResponse toResponse(Customer customer) {
        if (customer == null) return null;

        return CustomerResponse.builder()
                .id(customer.getId())
                .firstName(customer.getFirstName())
                .lastName(customer.getLastName())
                .email(customer.getEmail())
                .phone(customer.getPhone())
                .address(customer.getAddress())
                .status(customer.getStatus())
                .build();
    }

    @Override
    public List<CustomerResponse> toResponseList(List<Customer> customers) {
        if (customers == null) return null;
        return customers.stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    // ================= Dominio -> Entity =================

    @Override
    public CustomerEntity toEntity(Customer customer) {
        if (customer == null) return null;

        return CustomerEntity.builder()
                .id(customer.getId())
                .firstName(customer.getFirstName())
                .lastName(customer.getLastName())
                .email(customer.getEmail())
                .phone(customer.getPhone())
                .address(customer.getAddress())
                .status(customer.getStatus())
                .build();
    }

    // ================= Entity -> Dominio =================

    @Override
    public Customer toDomain(CustomerEntity entity) {
        if (entity == null) return null;

        return Customer.builder()
                .id(entity.getId())
                .firstName(entity.getFirstName())
                .lastName(entity.getLastName())
                .email(entity.getEmail())
                .phone(entity.getPhone())
                .address(entity.getAddress())
                .status(entity.getStatus())
                .build();
    }
}
