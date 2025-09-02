package com.imperialnet.stock_service.application.usecase;

import com.imperialnet.stock_service.application.dto.StockResponse;
import com.imperialnet.stock_service.application.port.in.GetStockByProductIdUseCase;
import com.imperialnet.stock_service.application.port.out.StockRepositoryPort;
import com.imperialnet.stock_service.domain.model.StockItem;
import com.imperialnet.stock_service.infraestructure.mapper.StockMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class GetStockByProductIdService implements GetStockByProductIdUseCase {

    private final StockRepositoryPort stockRepositoryPort;
    private final StockMapper stockMapper;

    @Override
    public StockResponse getStockByProductId(Long productId) {
        log.info("🔍 Buscando stock para productId={}", productId);

        StockItem stockItem = stockRepositoryPort.findByProductId(productId)
                .orElseThrow(() -> new IllegalArgumentException("Stock no encontrado para productId=" + productId));

        return stockMapper.toResponse(stockItem);
    }
}
