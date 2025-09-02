package com.imperialnet.stock_service.application.port.in;

import com.imperialnet.stock_service.application.dto.UpdateStockRequest;
import com.imperialnet.stock_service.application.dto.StockResponse;

public interface UpdateStockByProductIdUseCase {
    StockResponse updateStockByProductId(Long productId, UpdateStockRequest request);
}
