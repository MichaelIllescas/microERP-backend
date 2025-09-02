package com.imperialnet.stock_service.application.usecase;

import com.imperialnet.stock_service.application.dto.ProductCreatedEvent;
import com.imperialnet.stock_service.application.port.in.InitializeStockOnProductCreatedUseCase;
import com.imperialnet.stock_service.application.port.out.StockRepositoryPort;
import com.imperialnet.stock_service.domain.model.StockItem;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger; import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class InitializeStockOnProductCreatedService implements InitializeStockOnProductCreatedUseCase {
    private static final Logger log = LoggerFactory.getLogger(InitializeStockOnProductCreatedService.class);
    private final StockRepositoryPort stockRepository;

    @Override
    public void handle(ProductCreatedEvent event) {
        stockRepository.findByProductId(event.getProductId()).ifPresent(existing -> {
            log.info("Stock ya existe para productId={} -> no se crea de nuevo", event.getProductId());
            return;
        });
        int initial = event.getInitialQuantity() != null ? event.getInitialQuantity() : 0;
        StockItem toSave = StockItem.builder()
                .productId(event.getProductId())
                .quantity(initial)
                .status("ACTIVE")
                .build();
        stockRepository.save(toSave);
        log.info("Stock inicial creado para productId={} cantidad={}", event.getProductId(), initial);
    }
}
