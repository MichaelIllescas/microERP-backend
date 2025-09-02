package com.imperialnet.customer_service.application.usecase;

import com.imperialnet.customer_service.application.dto.CustomerResponse;
import com.imperialnet.customer_service.application.port.in.GetCustomerUseCase;
import com.imperialnet.customer_service.application.port.out.CustomerRepositoryPort;
import com.imperialnet.customer_service.domain.model.Customer;
import com.imperialnet.customer_service.infrastructure.mapper.CustomerMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class GetCustomerService implements GetCustomerUseCase {

    private final CustomerRepositoryPort repositoryPort;
    private final CustomerMapper mapper;

    @Override
    public CustomerResponse getCustomerById(Long id) {
        log.info("🔍 Buscando cliente con ID {}", id);
        Customer customer = repositoryPort.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Cliente no encontrado con ID " + id));
        return mapper.toResponse(customer);
    }

    @Override
    public List<CustomerResponse> getAllCustomers() {
        log.info("📋 Listando todos los clientes");
        List<Customer> customers = repositoryPort.findAll();
        return mapper.toResponseList(customers);
    }
}
