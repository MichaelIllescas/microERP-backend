package com.imperialnet.stock_service.infraestructure.adapter.in.messaging;

import com.imperialnet.stock_service.application.dto.ProductDeletedEvent;
import com.imperialnet.stock_service.application.port.in.DeleteStockByProductIdUseCase;
import com.imperialnet.stock_service.infraestructure.config.RabbitConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class ProductDeletedListener {

    private final DeleteStockByProductIdUseCase deleteStockByProductIdUseCase;

    @RabbitListener(queues = RabbitConfig.STOCK_QUEUE_DELETED)
    public void onProductDeleted(ProductDeletedEvent event) {
        deleteStockByProductIdUseCase.deleteStockByProductId(event);
    }
}
