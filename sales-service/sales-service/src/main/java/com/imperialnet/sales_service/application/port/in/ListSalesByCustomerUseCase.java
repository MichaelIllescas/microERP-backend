package com.imperialnet.sales_service.application.port.in;

import com.imperialnet.sales_service.application.dto.SaleResponse;
import java.util.List;

public interface ListSalesByCustomerUseCase {
    List<SaleResponse> listSalesByCustomer(Long customerId);
}
