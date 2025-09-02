package com.imperialnet.stock_service.infraestructure.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.DefaultClassMapper;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class RabbitConfig {
    public static final String PRODUCT_EXCHANGE = "product.exchange";
    public static final String STOCK_QUEUE_CREATED = "stock.queue.created";
    public static final String STOCK_QUEUE_DELETED = "stock.queue.deleted";
    public static final String ROUTING_KEY_CREATED = "product.created";
    public static final String ROUTING_KEY_DELETED = "product.deleted";

    @Bean
    public DirectExchange productExchange() {
        return new DirectExchange(PRODUCT_EXCHANGE);
    }

    @Bean
    public Queue stockQueueCreated() {
        return QueueBuilder.durable(STOCK_QUEUE_CREATED).build();
    }

    @Bean
    public Queue stockQueueDeleted() {
        return QueueBuilder.durable(STOCK_QUEUE_DELETED).build();
    }

    @Bean
    public Binding bindingCreated(Queue stockQueueCreated, DirectExchange productExchange) {
        return BindingBuilder.bind(stockQueueCreated)
                .to(productExchange)
                .with(ROUTING_KEY_CREATED);
    }

    @Bean
    public Binding bindingDeleted(Queue stockQueueDeleted, DirectExchange productExchange) {
        return BindingBuilder.bind(stockQueueDeleted)
                .to(productExchange)
                .with(ROUTING_KEY_DELETED);
    }

    // 👇 ClassMapper para resolver tipos de eventos aunque vengan de otro paquete
    @Bean
    public DefaultClassMapper classMapper() {
        DefaultClassMapper classMapper = new DefaultClassMapper();

        Map<String, Class<?>> idClassMapping = new HashMap<>();
        idClassMapping.put("com.imperialnet.product_service.application.dto.ProductDeletedEvent",
                com.imperialnet.stock_service.application.dto.ProductDeletedEvent.class);
        idClassMapping.put("com.imperialnet.product_service.application.dto.ProductCreatedEvent",
                com.imperialnet.stock_service.application.dto.ProductCreatedEvent.class);

        classMapper.setIdClassMapping(idClassMapping);
        classMapper.setTrustedPackages("*"); // confiar en todos los paquetes
        return classMapper;
    }

    @Bean
    public MessageConverter jsonMessageConverter(DefaultClassMapper classMapper) {
        Jackson2JsonMessageConverter converter = new Jackson2JsonMessageConverter();
        converter.setClassMapper(classMapper);
        return converter;
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory,
                                         MessageConverter messageConverter) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(messageConverter);
        return template;
    }
}
