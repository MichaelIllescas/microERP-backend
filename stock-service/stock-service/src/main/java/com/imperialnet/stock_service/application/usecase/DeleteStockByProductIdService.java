package com.imperialnet.stock_service.application.usecase;

import com.imperialnet.stock_service.application.dto.ProductDeletedEvent;
import com.imperialnet.stock_service.application.port.in.DeleteStockByProductIdUseCase;
import com.imperialnet.stock_service.application.port.out.StockRepositoryPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class DeleteStockByProductIdService implements DeleteStockByProductIdUseCase {

    private final StockRepositoryPort stockRepositoryPort;

    @Override
    @Transactional
    public void deleteStockByProductId(ProductDeletedEvent event) {
        log.info("📥 Procesando evento ProductDeleted: {}", event);

        stockRepositoryPort.findByProductId(event.getProductId()).ifPresentOrElse(stock -> {
            stockRepositoryPort.deleteByProductId(event.getProductId());
            log.info("🗑️ Stock eliminado para productId={}", event.getProductId());
        }, () -> {
            log.warn("⚠️ No se encontró stock para productId={} (nada que eliminar)", event.getProductId());
        });
    }
}
