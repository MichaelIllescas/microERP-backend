package com.imperialnet.product_service.application.event;

import com.imperialnet.product_service.application.dto.ProductCreatedEvent;
import com.imperialnet.product_service.application.dto.ProductDeletedEvent;
import com.imperialnet.product_service.infrastructure.config.RabbitConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class ProductEventPublisher {

    private final RabbitTemplate rabbitTemplate;

    public void publishProductCreated(ProductCreatedEvent event) {
        log.info("📨 Publicando evento ProductCreated: {}", event);
        rabbitTemplate.convertAndSend(
                RabbitConfig.PRODUCT_EXCHANGE,
                RabbitConfig.ROUTING_KEY,
                event
        );
    }

    public void publishProductDeleted(ProductDeletedEvent event) {
        log.info("📨 Publicando evento ProductDeleted: {}", event);
        rabbitTemplate.convertAndSend(
                RabbitConfig.PRODUCT_EXCHANGE,
                RabbitConfig.ROUTING_KEY_DELETED,
                event
        );
    }
}
