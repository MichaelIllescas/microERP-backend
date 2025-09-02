package com.imperialnet.customer_service.application.port.in;

import com.imperialnet.customer_service.application.dto.UpdateCustomerRequest;
import com.imperialnet.customer_service.application.dto.CustomerResponse;

public interface UpdateCustomerUseCase {
    CustomerResponse updateCustomer(Long id, UpdateCustomerRequest request);
}
