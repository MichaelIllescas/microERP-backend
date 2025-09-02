package com.imperialnet.stock_service.infraestructure.adapter.in.messaging;

import com.imperialnet.stock_service.application.dto.ProductCreatedEvent;
import com.imperialnet.stock_service.application.port.in.InitializeStockOnProductCreatedUseCase;
import com.imperialnet.stock_service.infraestructure.config.RabbitConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ProductCreatedListener {

    private final InitializeStockOnProductCreatedUseCase useCase;

    @RabbitListener(queues = RabbitConfig.STOCK_QUEUE_CREATED)
    public void onProductCreated(ProductCreatedEvent event) {
        useCase.handle(event);
    }
}
