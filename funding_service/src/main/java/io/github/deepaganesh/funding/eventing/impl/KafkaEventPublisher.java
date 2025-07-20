package io.github.deepaganesh.funding.eventing.impl;

import io.github.deepaganesh.funding.dto.InvoiceFundedEvent;
import io.github.deepaganesh.funding.eventing.EventPublisher;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@Profile("kafka")
@Slf4j
public class KafkaEventPublisher implements EventPublisher {

    @Autowired
    KafkaTemplate<String, InvoiceFundedEvent> kafkaTemplate;

    @Value("${app.kafka.topic.invoice-funded-topic}")
    private String topic;

    @Override
    public void publishInvoiceFunded(InvoiceFundedEvent event) {
        String key = String.valueOf(event.getInvoiceId());
        kafkaTemplate.send(topic, key, event);
        log.info("Published to Kafka - Key: {}",  key);
    }
}
