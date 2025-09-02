package com.imperialnet.stock_service.application.port.in;

import com.imperialnet.stock_service.application.dto.ProductCreatedEvent;

public interface InitializeStockOnProductCreatedUseCase {
    void handle(ProductCreatedEvent event);
}
