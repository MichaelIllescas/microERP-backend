package com.imperialnet.customer_service.infrastructure.mapper;

import com.imperialnet.customer_service.application.dto.*;
import com.imperialnet.customer_service.domain.model.Customer;
import com.imperialnet.customer_service.infrastructure.persistence.entity.CustomerEntity;

import java.util.List;

/**
 * Interfaz para convertir entre DTOs, dominio y entidades JPA.
 */
public interface CustomerMapper {

    // DTO -> Dominio
    Customer toDomain(CreateCustomerRequest request);
    Customer toDomain(UpdateCustomerRequest request);

    // Dominio -> DTO
    CustomerResponse toResponse(Customer customer);
    List<CustomerResponse> toResponseList(List<Customer> customers);

    // Dominio -> Entity
    CustomerEntity toEntity(Customer customer);

    // Entity -> Dominio
    Customer toDomain(CustomerEntity entity);
}
