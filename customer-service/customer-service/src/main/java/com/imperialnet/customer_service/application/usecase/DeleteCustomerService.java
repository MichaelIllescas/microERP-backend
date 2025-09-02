package com.imperialnet.customer_service.application.usecase;

import com.imperialnet.customer_service.application.port.in.DeleteCustomerUseCase;
import com.imperialnet.customer_service.application.port.out.CustomerRepositoryPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class DeleteCustomerService implements DeleteCustomerUseCase {

    private final CustomerRepositoryPort repositoryPort;

    @Override
    public void deleteCustomer(Long id) {
        log.warn("🗑️ Eliminando cliente con ID {}", id);

        if (repositoryPort.findById(id).isEmpty()) {
            throw new IllegalArgumentException("Cliente no encontrado con ID " + id);
        }

        repositoryPort.deleteById(id);

        log.info("✅ Cliente eliminado con ID {}", id);
    }
}
