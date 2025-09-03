package com.imperialnet.sales_service.application.port.in;

import com.imperialnet.sales_service.application.dto.CreateSaleRequest;
import com.imperialnet.sales_service.application.dto.SaleResponse;

public interface CreateSaleUseCase {

    public SaleResponse registerSale(CreateSaleRequest request);
}
