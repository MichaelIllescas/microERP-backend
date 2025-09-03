package com.imperialnet.sales_service.application.port.in;

import com.imperialnet.sales_service.application.dto.SaleResponse;

import java.time.LocalDate;
import java.util.List;

public interface ListSalesByDateRangeUseCase {
    List<SaleResponse> listSalesByDateRange(LocalDate startDate, LocalDate endDate);
}
