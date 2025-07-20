package io.github.deepaganesh.funding.eventing.impl;

import io.github.deepaganesh.funding.dto.InvoiceFundedEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import io.github.deepaganesh.funding.eventing.EventPublisher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

@Service
@Profile("local")
@Slf4j
public class RabbitMQEventPublisher implements EventPublisher {

    @Autowired
    RabbitTemplate rabbitTemplate;

    @Value("${app.rabbitmq.exchange}")
    private String exchange;

    @Value("${app.rabbitmq.routing-key}")
    private String routingKey;

    @Override
    public void publishInvoiceFunded(InvoiceFundedEvent event) {
        rabbitTemplate.convertAndSend(exchange, routingKey, event);
        log.info("Published to RabbitMQ - Key: {} ", routingKey);
    }
}
