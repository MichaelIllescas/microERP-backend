package com.imperialnet.stock_service.application.usecase;

import com.imperialnet.stock_service.application.dto.UpdateStockRequest;
import com.imperialnet.stock_service.application.dto.StockResponse;
import com.imperialnet.stock_service.application.port.in.UpdateStockByProductIdUseCase;
import com.imperialnet.stock_service.application.port.out.StockRepositoryPort;
import com.imperialnet.stock_service.domain.model.StockItem;
import com.imperialnet.stock_service.infraestructure.mapper.StockMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class UpdateStockByProductIdService implements UpdateStockByProductIdUseCase {

    private final StockRepositoryPort stockRepositoryPort;
    private final StockMapper stockMapper;

    @Override
    public StockResponse updateStockByProductId(Long productId, UpdateStockRequest request) {
        log.info("✏️ Actualizando stock para productId={} con cantidad={}", productId, request.getQuantity());

        StockItem stockItem = stockRepositoryPort.findByProductId(productId)
                .orElseThrow(() -> new IllegalArgumentException("Producto no encontrado en stock: " + productId));

        stockMapper.applyUpdate(stockItem, request);

        StockItem saved = stockRepositoryPort.save(stockItem);

        return stockMapper.toResponse(saved);
    }
}
