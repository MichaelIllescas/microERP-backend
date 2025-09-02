package com.imperialnet.customer_service.application.port.in;

import com.imperialnet.customer_service.application.dto.CustomerResponse;

import java.util.List;

public interface GetCustomerUseCase {
    CustomerResponse getCustomerById(Long id);
    List<CustomerResponse> getAllCustomers();
}
