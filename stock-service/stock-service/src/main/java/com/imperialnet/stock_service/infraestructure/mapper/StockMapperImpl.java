package com.imperialnet.stock_service.infraestructure.mapper;

import com.imperialnet.stock_service.domain.model.StockItem;
import com.imperialnet.stock_service.application.dto.UpdateStockRequest;
import com.imperialnet.stock_service.application.dto.StockResponse;
import org.springframework.stereotype.Component;

@Component
public class StockMapperImpl implements StockMapper {

    @Override
    public StockItem applyUpdate(StockItem stockItem, UpdateStockRequest request) {
        if (stockItem == null || request == null) {
            return stockItem;
        }

        stockItem.setQuantity(request.getQuantity());
        return stockItem;
    }

    @Override
    public StockResponse toResponse(StockItem stockItem) {
        if (stockItem == null) {
            return null;
        }

        return StockResponse.builder()
                .productId(stockItem.getProductId())
                .quantity(stockItem.getQuantity())
                .build();
    }
}
