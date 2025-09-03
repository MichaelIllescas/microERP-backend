package com.imperialnet.sales_service.application.port.in;

import com.imperialnet.sales_service.application.dto.CreateSaleRequest;
import com.imperialnet.sales_service.application.dto.SaleResponse;

import java.util.List;

public interface CreateSaleUseCase {

    public SaleResponse registerSale(CreateSaleRequest request);

    interface ListSalesUseCase {
        List<SaleResponse> listSales(String status);
    }
}
