package com.imperialnet.customer_service.application.usecase;

import com.imperialnet.customer_service.application.dto.UpdateCustomerRequest;
import com.imperialnet.customer_service.application.dto.CustomerResponse;
import com.imperialnet.customer_service.application.port.in.UpdateCustomerUseCase;
import com.imperialnet.customer_service.application.port.out.CustomerRepositoryPort;
import com.imperialnet.customer_service.domain.model.Customer;
import com.imperialnet.customer_service.infrastructure.mapper.CustomerMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class UpdateCustomerService implements UpdateCustomerUseCase {

    private final CustomerRepositoryPort repositoryPort;
    private final CustomerMapper mapper;

    @Override
    public CustomerResponse updateCustomer(Long id, UpdateCustomerRequest request) {
        log.info("✏️ Actualizando cliente con ID {}", id);

        Customer existing = repositoryPort.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Cliente no encontrado con ID " + id));

        // Solo actualizamos los campos que vienen no nulos
        if (request.getFirstName() != null) {
            existing.setFirstName(request.getFirstName());
        }
        if (request.getLastName() != null) {
            existing.setLastName(request.getLastName());
        }
        if (request.getEmail() != null) {
            existing.setEmail(request.getEmail());
        }
        if (request.getPhone() != null) {
            existing.setPhone(request.getPhone());
        }
        if (request.getAddress() != null) {
            existing.setAddress(request.getAddress());
        }
        if (request.getStatus() != null) {
            existing.setStatus(request.getStatus());
        }

        Customer updated = repositoryPort.save(existing);

        log.info("✅ Cliente actualizado parcialmente con ID {}", updated.getId());
        return mapper.toResponse(updated);
    }
}
