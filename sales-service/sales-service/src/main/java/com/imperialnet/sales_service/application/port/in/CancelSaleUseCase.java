package com.imperialnet.sales_service.application.port.in;

import com.imperialnet.sales_service.application.dto.SaleResponse;

public interface CancelSaleUseCase {
    SaleResponse cancelSale(Long id);
}
