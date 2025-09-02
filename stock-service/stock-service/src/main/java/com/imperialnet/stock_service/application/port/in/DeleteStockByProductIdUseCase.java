package com.imperialnet.stock_service.application.port.in;

import com.imperialnet.stock_service.application.dto.ProductDeletedEvent;

public interface DeleteStockByProductIdUseCase {
    void deleteStockByProductId(ProductDeletedEvent event);
}
