package com.imperialnet.stock_service.application.usecase;

import com.imperialnet.stock_service.application.dto.StockResponse;
import com.imperialnet.stock_service.application.port.in.GetAllStockUseCase;
import com.imperialnet.stock_service.application.port.in.GetStockByProductIdUseCase;
import com.imperialnet.stock_service.application.port.out.StockRepositoryPort;
import com.imperialnet.stock_service.domain.model.StockItem;
import com.imperialnet.stock_service.infraestructure.mapper.StockMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ListAllStockService implements GetAllStockUseCase {

    private final StockRepositoryPort stockRepositoryPort;
    private final StockMapper stockMapper;

    @Override
    public List<StockResponse> getAllStock() {
        log.info("📦 Listando todo el stock");

        List<StockItem> stockItems = stockRepositoryPort.findAll();

        return stockItems.stream()
                .map(stockMapper::toResponse)
                .collect(Collectors.toList());
    }
}
