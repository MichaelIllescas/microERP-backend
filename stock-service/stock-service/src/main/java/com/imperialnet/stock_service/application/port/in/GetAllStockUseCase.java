package com.imperialnet.stock_service.application.port.in;

import com.imperialnet.stock_service.application.dto.StockResponse;
import java.util.List;

public interface GetAllStockUseCase {
    List<StockResponse> getAllStock();
}
