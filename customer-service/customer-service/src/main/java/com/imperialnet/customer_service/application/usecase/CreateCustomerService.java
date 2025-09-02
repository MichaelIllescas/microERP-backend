package com.imperialnet.customer_service.application.usecase;

import com.imperialnet.customer_service.application.dto.CreateCustomerRequest;
import com.imperialnet.customer_service.application.dto.CustomerResponse;
import com.imperialnet.customer_service.application.port.in.CreateCustomerUseCase;
import com.imperialnet.customer_service.application.port.out.CustomerRepositoryPort;
import com.imperialnet.customer_service.domain.model.Customer;
import com.imperialnet.customer_service.infrastructure.mapper.CustomerMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * Caso de uso: Crear un cliente
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class CreateCustomerService implements CreateCustomerUseCase {

    private final CustomerRepositoryPort repositoryPort;
    private final CustomerMapper mapper;

    @Override
    public CustomerResponse createCustomer(CreateCustomerRequest request) {
        log.info("▶️ Iniciando creación de cliente: {}", request.getEmail());

        if (repositoryPort.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("Ya existe un cliente con el email: " + request.getEmail());
        }

        Customer customer = mapper.toDomain(request);
        Customer saved = repositoryPort.save(customer);

        log.info("✅ Cliente creado con ID {}", saved.getId());
        return mapper.toResponse(saved);
    }
}
