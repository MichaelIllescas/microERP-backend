package com.imperialnet.stock_service.application.port.in;

import com.imperialnet.stock_service.application.dto.StockResponse;

public interface GetStockByProductIdUseCase {
    StockResponse getStockByProductId(Long productId);
}
