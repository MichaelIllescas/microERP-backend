package com.imperialnet.stock_service.infraestructure.mapper;

import com.imperialnet.stock_service.domain.model.StockItem;
import com.imperialnet.stock_service.application.dto.UpdateStockRequest;
import com.imperialnet.stock_service.application.dto.StockResponse;

public interface StockMapper {
    
    StockItem applyUpdate(StockItem stockItem, UpdateStockRequest request);

    StockResponse toResponse(StockItem stockItem);
}
