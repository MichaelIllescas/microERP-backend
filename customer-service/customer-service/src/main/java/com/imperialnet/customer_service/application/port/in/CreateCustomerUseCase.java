package com.imperialnet.customer_service.application.port.in;

import com.imperialnet.customer_service.application.dto.CreateCustomerRequest;
import com.imperialnet.customer_service.application.dto.CustomerResponse;

public interface CreateCustomerUseCase {
    CustomerResponse createCustomer(CreateCustomerRequest request);
}
