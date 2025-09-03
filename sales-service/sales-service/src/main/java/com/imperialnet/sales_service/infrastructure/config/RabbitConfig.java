package com.imperialnet.sales_service.infrastructure.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {

    public static final String SALES_EXCHANGE = "sales.exchange";
    public static final String SALES_NOTIFICATION_QUEUE = "sales.notification.queue";
    public static final String SALES_ROUTING_KEY = "sales.created";

    @Bean
    public TopicExchange salesExchange() {
        return new TopicExchange(SALES_EXCHANGE, true, false);
    }

    @Bean
    public Queue salesNotificationQueue() {
        return new Queue(SALES_NOTIFICATION_QUEUE, true);
    }

    @Bean
    public Binding binding() {
        return BindingBuilder.bind(salesNotificationQueue())
                .to(salesExchange())
                .with(SALES_ROUTING_KEY);
    }

    // 👇 Converter para serializar objetos en JSON
    @Bean
    public Jackson2JsonMessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    // 👇 RabbitTemplate con soporte JSON
    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory,
                                         Jackson2JsonMessageConverter messageConverter) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(messageConverter);
        return template;
    }
}
