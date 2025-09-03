package com.imperialnet.sales_service.application.port.in;

import com.imperialnet.sales_service.application.dto.SaleResponse;

public interface GetSaleByIdUseCase {
    SaleResponse getSaleById(Long id);
}
